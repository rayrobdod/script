package com.rayrobdod.scriptSample


/** A simple game state */
final case class State(
	/** A character's name */
	val name:String,
	/** A set of flags */
	val flags:Map[String, Int]
)

object State {
	/** A state containing an empty name field and no set flags */
	val empty = new State("", Map.empty)
	
	
	/** A function that will create a new state from an old state, but which has a new flag. */
	val SetFlag = {(s:State, name:String, value:Int) => {
		State(
			s.name,
			s.flags + ((name, value))
		)
	}}
}
