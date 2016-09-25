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
package com.rayrobdod.scriptSample

import scala.runtime.{AbstractFunction1 => AFunction1}
import com.codecommit.antixml.{QName, Attributes => XmlAttrs}
import com.rayrobdod.script.constTrue
import com.rayrobdod.script.parser.{AttrsToUseFun => BaseAttrsToUseFun}



/**
 * A function that will look at an xml Elem's attributes, and will
 * determine whether the corresponding ScriptElement should be used  
 */
object AttrsToUseFun extends BaseAttrsToUseFun[State] {
	
	def apply(attrs:XmlAttrs):Function1[State, Boolean] = {
		PolyAnd(attrs.map{_ match {
				case Tuple2(QName(_, "if-not-flag"), flag) =>
					{(x:State) => x.flags.get(flag).getOrElse(0) == 0}
				case Tuple2(QName(_, "if-flag"), flag) =>
					{(x:State) => x.flags.get(flag).getOrElse(0) != 0}
				case _ => constTrue
		}})
	}



	private object PolyAnd {
		def apply[A](funs:Iterable[Function1[A, Boolean]]):Function1[A, Boolean] = {
			val funs2 = funs.filter{constTrue != _}.toList
			
			funs2 match {
				case Nil => constTrue
				case x :: Nil => x
				case _ => new PolyAnd(funs2)
			}
		}
	}
	
	private class PolyAnd[A](funs:Iterable[Function1[A, Boolean]])
				extends AFunction1[A, Boolean] {
		def apply(a:A):Boolean = funs.forall{_(a)}
		
		override def toString:String = "PolyAnd(" + funs.toString + ")"
	}
}
