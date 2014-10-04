package com.rayrobdod.script

import scala.collection.immutable.{Seq, Set, Map}

/**
 * Superclass of all script elements
 */
trait ScriptElement[-State] {
	def use(s:State):Boolean
}

/**
 * A script element that contains other script elements
 */
final case class Group[State](
	val elems:Seq[ScriptElement[State]],
	useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	def use(s:State) = useFun(s)
}

/**
 * A script element that contains other script elements
 */
final case class Speak[State](
	val speaker:String,
	val emotion:String,
	val words:String,
	useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	def use(s:State) = useFun(s)
}

/**
 * A script element that contains other script elements
 * @todo would setTo:(Int) => Int` be useful?
 */
final case class SetFlag[State](
	val flag:String,
	val setTo:Int,
	useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	def use(s:State) = useFun(s)
}

/**
 * The most basic of choices - Yes/No.
 * 
 * Expected result - set flag based on a prompt.
 */
case class YesNo[State](
		val flag:String,
		useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	def use(s:State) = useFun(s)
}


/**
 * A no-op
 */
case object NoOp extends ScriptElement[Any] {
	override def use(s:Any):Boolean = false
}
