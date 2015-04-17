/*
	Copyright (c) 2015, Raymond Dodge
	All rights reserved.
	
	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met:
		* Redistributions of source code must retain the above copyright
		  notice, this list of conditions and the following disclaimer.
		* Redistributions in binary form must reproduce the above copyright
		  notice, this list of conditions and the following disclaimer in the
		  documentation and/or other materials provided with the distribution.
		* Neither the name "<PRODUCT NAME>" nor the names of its contributors
		  may be used to endorse or promote products derived from this software
		  without specific prior written permission.
	
	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
	DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
	DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.rayrobdod.script;
package parser

import scala.runtime.{AbstractFunction1 => AFunction1}
import com.codecommit.antixml.{Elem, Selector, Text,
		QName, Node, Attributes => XmlAttrs, XML}
import java.net.URL

/**
 * Constructs base script elements from xml
 */
object BaseScriptFromXml extends ScriptFromXml {
	
	/**
	 * Constructs base script elements from xml
	 * @param useFun Determines, based on an Xml Element's attributes,
	 *			a function that determines whether, based on a program's
	 * 			state, whether a script element is used or not.
	 * @param xml the element to convert
	 * @param base the base url used when constructing links
	 */
	override def apply[A](
			useFun:AttrsToUseFun[A],
			xml:Elem,
			base:URL,
			recurser:ScriptFromXml
	):ScriptElement[A] = xml match {
		case Elem(_, "speak", attrs, _, _) => {
			
			new Speak[A](
					attrs("speaker"),
					"",
					(xml \\ text).foldLeft(""){_ + ' ' + _}.trim,
					useFun(attrs)
			)
		}
		case Elem(_, "group", attrs, _, children) => {
			
			new Group[A](
				(xml \ elems).map{recurser.apply(useFun, _, base, recurser)},
				useFun(attrs)
			)
		}
		case Elem(_, "setFlag", attrs, _, children) => {
			
			val setToValue = attrs.getOrElse("value", "1").toInt
			
			new SetFlag[A](
				attrs("flag"),
				setToValue,
				useFun(attrs)
			)
		}
		case Elem(_, "yesNo", attrs, _, children) => {
			
			new YesNo[A](
				attrs("flag"),
				useFun(attrs)
			)
		}
		case Elem(_, "options", attrs, _, children) => {
			
			val childs = xml \ elems
			val keys = childs.map{_.attrs("optionName")}
			val vals = childs.map{recurser.apply(useFun, _, base, recurser)}
			
			new Options[A](
				keys.zip(vals),
				useFun(attrs)
			)
		}
		case Elem(_, "goto", attrs, _, children) => {
			
			val newUrl = base.toURI.resolve(attrs("href")).toURL
			GoTo(new GotoFunction[A](useFun, newUrl, recurser), useFun(attrs))
			
		}
		case Elem(_, "noOp", attrs, _, children) => {
			
			NoOp
		}
		case Elem(_, name, _, _, _) => {
			
			throw new IllegalArgumentException("Unexpected element: " + name)
		}
		case _ => {
			
			throw new IllegalArgumentException("Not an element ")
		}
	}
	
	/**
	 * Returns true iff apply will not throw an error if presented with
	 * this Xml Element
	 */
	override def isDefinedAt(xml:Elem):Boolean = xml match {
		case Elem(_, name, _, _, _) => name match {
			case "speak" => true
			case "group" => true
			case "setFlag" => true
			case "yesNo" => true
			case "options" => true
			case "goto" => true
			case "noOp" => true
			case _ => false
		}
		case _ => false
	}
	
	private val text:Selector[String] = Selector({case Text(str) => xmlNormalize(str)})
	private val emote:Selector[String] = Selector({case Elem(Some("emote"), str, _, _, _) => str})
	private object elems extends Selector[Elem]{
		def apply(x:Node):Elem  = x match {case y:Elem => y; case _ => null}
		def isDefinedAt(x:Node) = x match {case y:Elem => true; case _ => false}
	}
	
	
	
	
	
	
	private def xmlNormalize(s:String):String = {
		import java.util.regex.Pattern.{compile => Pattern}
		val spacePattern = Pattern("""[\s]+""")
		
		spacePattern.matcher(s.trim).replaceAll(" ")
	}
	
	private class GotoFunction[A](useFun:AttrsToUseFun[A], url:URL, recurser:ScriptFromXml) extends Function0[ScriptElement[A]] {
		def apply() = {
			val fileXml = {
				val stream = url.openStream
				val xml = XML.fromInputStream(url.openStream)
				stream.close()
				xml
			}
			
			val myXml = if (null == url.getRef) {
				fileXml
			} else {
				object idMatches extends Selector[Elem]{
					def apply(x:Node):Elem  = x match {case y:Elem => y; case _ => null}
					def isDefinedAt(x:Node) = x match {
						case y:Elem =>
							(y.attrs contains "id") && (y.attrs("id") == url.getRef)
						case _ => false
					}
				}
				
				(fileXml \\ idMatches).head
			}
			
			// todo: xml:base, either here or in antixml
			recurser.apply(useFun, myXml, url, recurser)
		}
	}
}
