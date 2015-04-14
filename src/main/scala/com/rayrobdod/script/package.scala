package com.rayrobdod

import scala.collection.immutable.{Seq, Set, Map}
import scala.runtime.{AbstractFunction1 => AFunction1}

package object script {
	
	
	/** A function that always returns true */
	object constTrue extends AFunction1[Any, Boolean] { 
		def apply(a:Any) = true
		override def toString = "constTrue"
	}
}
