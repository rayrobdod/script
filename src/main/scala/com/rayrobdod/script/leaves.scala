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
package com.rayrobdod.script

import scala.collection.immutable.{Seq, Set, Map}

/**
 * A script element that represents an entity speaking
 * 
 * @constructor
 * @param speaker the entity that is speaking
 * @param emotion the emotion of the entity that is speaking
 * @param words the spoken words
 * @param useFun $useFun
 */
final case class Speak[State](
	val speaker:String,
	val emotion:String,
	val words:String,
	useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	final override def use(s:State):Boolean = useFun(s)
}

/**
 * Sets a flag in the current state.
 * 
 * @todo would setTo:(Int) => Int` be useful?
 * @constructor
 * @param flag the name of the flag to set
 * @param setTo the value to set the flag to.
 * @param useFun $useFun
 */
final case class SetFlag[State](
	val flag:String,
	val setTo:Int,
	useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	final override def use(s:State):Boolean = useFun(s)
}

/**
 * Prompts the user to enter Yes or No.
 * 
 * It doesn't ask anything, so
 * whatever the Y/N is a response to should have been printed using
 * a [[com.rayrobdod.script.Speak]] or similar already.
 * 
 * @constructor
 * @param flag the name of the flag to set
 * @param useFun $useFun
 */
final case class YesNo[State](
		val flag:String,
		useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	final override def use(s:State):Boolean = useFun(s)
}

/**
 * A no-op
 */
case object NoOp extends ScriptElement[Any] {
	override def use(s:Any):Boolean = true
}
