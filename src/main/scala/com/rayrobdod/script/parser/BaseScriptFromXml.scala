package com.rayrobdod.script;
package parser

import scala.runtime.{AbstractFunction1 => AFunction1}
import com.codecommit.antixml.{Elem, Selector, Text,
		QName, Node, Attributes => XmlAttrs}


/**
 */
object BaseScriptFromXml /* extends Function1[Elem, ScriptElement] */ {
	
	def apply[A](useFun:AttrsToUseFun[A], xml:Elem):ScriptElement[A] = xml match {
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
				(xml \ scriptElems).map{BaseScriptFromXml.apply(useFun, _)},
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
	
	def isDefinedAt(xml:Elem):Boolean = xml match {
		case Elem(_, name, _, _, _) => name match {
			case "speak" => true
			case "group" => true
			case "setFlag" => true
			case "yesNo" => true
			case "noOp" => true
			case _ => false
		}
		case _ => false
	}
	
	val text:Selector[String] = Selector({case Text(str) => xmlNormalize(str)})
	private val emote:Selector[String] = Selector({case Elem(Some("emote"), str, _, _, _) => str})
	private object scriptElems extends Selector[Elem]{
		def apply(x:Node):Elem = {x match {case y:Elem => y; case _ => null}}
		
		def isDefinedAt(x:Node) = x match {
			case Elem(_, elemName, _, _, _) => {
				Seq("group", "speak", "setFlag", "yesNo", "noOp").contains(elemName)
			}
			case _ => false
		}
	}
	
	
	trait AttrsToUseFun[State] {
		def apply(attrs:XmlAttrs):Function1[State,Boolean]
	}
	
	
	
	
	
	
	private def xmlNormalize(s:String):String = {
		import java.util.regex.Pattern.{compile => Pattern}
		val spacePattern = Pattern("""[\s]+""")
		
		spacePattern.matcher(s.trim).replaceAll(" ")
	}
}
