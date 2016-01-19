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
 * A container for other script elements. Each inner script element
 * is executed sequentially
 * 
 * @constructor
 * @param elems the contained scriptElements
 * @param useFun $useFun
 */
final case class Group[State](
	val elems:Seq[ScriptElement[State]],
	useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	final override def use(s:State):Boolean = useFun(s)
}

/**
 * Prompts the user to enter one of several options. It executes only
 * the ScriptElement corresponding to the item the user selected.
 
 * @constructor
 * @param options the options
 * @param useFun $useFun
 */
final case class Options[State](
		val options:Seq[(String, ScriptElement[State])],
		useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	final override def use(s:State):Boolean = useFun(s)
}

/**
 * A script element that allows indirection - either for the purposes
 * of deferred execution, or for creating looping constructs
 
 * @constructor
 * @param href a function pointing to the ScriptElement that this
 		will execute
 * @param useFun $useFun
 */
final case class GoTo[State](
		val href:() => ScriptElement[State],
		useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	final override def use(s:State):Boolean = useFun(s)
}
