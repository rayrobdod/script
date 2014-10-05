package com.rayrobdod.script

import scala.collection.immutable.{Seq, Set, Map}

/**
 * A script element
 * 
 * @define useFun a function called directly by the use method
 */
trait ScriptElement[-State] {
	/**
	 * true if the interface should present and show the effects of
	 * this element, and false otherwise
	 */
	def use(s:State):Boolean
}

/**
 * A container for other script elements. Each internal script element
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
	final override def use(s:State) = useFun(s)
}

/**
 * A script element that contains other script elements
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
	final override def use(s:State) = useFun(s)
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
	final override def use(s:State) = useFun(s)
}

/**
 * Prompts the user to enter Yes or No. It doesn't ask anything,
 * whatever the Y/N is a response to should have been printed using
 * a Speak or similar already.
 * 
 * @constructor
 * @param flag the name of the flag to set
 * @param useFun $useFun
 */
final case class YesNo[State](
		val flag:String,
		useFun:Function1[State,Boolean] = constTrue
) extends ScriptElement[State] {
	final override def use(s:State) = useFun(s)
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
	final override def use(s:State) = useFun(s)
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
	final override def use(s:State) = useFun(s)
}

/**
 * A no-op
 */
case object NoOp extends ScriptElement[Any] {
	override def use(s:Any):Boolean = true
}
