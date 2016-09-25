package com.rayrobdod.scriptSample

import java.io.{StringWriter, StringReader}
import scala.collection.immutable.{Seq, Set, Map}
import org.scalatest.{FunSuite, FunSpec}
import org.scalatest.prop.PropertyChecks
import com.codecommit.antixml.Attributes
import com.rayrobdod.script.constTrue

class AttrsToUseFunTest extends FunSpec {
	
	describe("AttrsToUseFun") {
		it ("empty Attrs results in constTrue") {
			assertResult(constTrue){AttrsToUseFun(Attributes())}
		}
		it ("Attrs with unrecognised attributes results in constTrue") {
			assertResult(constTrue){AttrsToUseFun(Attributes("other" -> "whee"))}
		}
		describe ("`if-flag=hello` results in a function that") {
			val flag = "hello"
			val fun = AttrsToUseFun(Attributes("if-flag" -> flag))
			
			it ("is false if 'hello' is not a key the flags map") {
				val state = State("bob", Map.empty)
				assert( !fun(state) )
			}
			it ("is true if 'hello -> 1' is in the flags map") {
				val state = State("bob", Map(flag -> 1))
				assert( fun(state) )
			}
			it ("is true if 'hello -> 98' is in the flags map") {
				val state = State("bob", Map(flag -> 98))
				assert( fun(state) )
			}
			it ("is false if 'hello -> 0' is in the flags map") {
				val state = State("bob", Map(flag -> 0))
				assert( !fun(state) )
			}
			it ("is false if 'adsf -> 1' is in the flags map") {
				val state = State("bob", Map("asdf" -> 1))
				assert( !fun(state) )
			}
		}
		describe ("`if-not-flag=hello` results in a function that") {
			val flag = "hello"
			val fun = AttrsToUseFun(Attributes("if-not-flag" -> flag))
			
			it ("is true if 'hello' is not a key the flags map") {
				val state = State("bob", Map.empty)
				assert( fun(state) )
			}
			it ("is false if 'hello -> 1' is in the flags map") {
				val state = State("bob", Map(flag -> 1))
				assert( !fun(state) )
			}
			it ("is true if 'adsf -> 1' is in the flags map") {
				val state = State("bob", Map("asdf" -> 1))
				assert( fun(state) )
			}
		}
		describe ("`if-not-flag=hello if-flag=world` results in a function that") {
			val notFlag = "hello"
			val flag = "world"
			val fun = AttrsToUseFun(Attributes("if-not-flag" -> notFlag, "if-flag" -> flag))
			
			it ("is false if given an empty map") {
				val state = State("bob", Map.empty)
				assert( !fun(state) )
			}
			it ("is true if 'world -> 1' is in the flags map and hello is not in the flag keys") {
				val state = State("bob", Map(flag -> 1))
				assert( fun(state) )
			}
			it ("is false if 'world -> 1; hello -> 1' is in the flags map") {
				val state = State("bob", Map(flag -> 1, notFlag -> 1))
				assert( !fun(state) )
			}
			it ("is false if 'hello -> 1' is in the flags map") {
				val state = State("bob", Map(notFlag -> 1))
				assert( !fun(state) )
			}
		}
	}
}
