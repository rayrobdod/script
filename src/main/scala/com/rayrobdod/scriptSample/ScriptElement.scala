package com.rayrobdod.scriptSample

import com.rayrobdod.script.ScriptElement



/**
 * Prompts the user for a name
 */
final case object SetName extends ScriptElement[Any] {
	def use(s:Any) = true
}

/**
 * Repeats the user's name
 */
final case object PrintName extends ScriptElement[Any] {
	def use(s:Any) = true
}

/**
 * Prompts the user to enter Boy or Girl.
 */
final case object SetGender extends ScriptElement[Any] {
	def use(s:Any) = true
}
