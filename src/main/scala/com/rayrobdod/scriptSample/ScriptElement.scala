package com.rayrobdod.scriptSample

import com.rayrobdod.script.ScriptElement



/**
 * Prompts the user for a name
 */
final case object SetName extends ScriptElement[State] {
	def use(s:State) = true
}

/**
 * Repeats the user's name
 */
final case object PrintName extends ScriptElement[State] {
	def use(s:State) = true
}
