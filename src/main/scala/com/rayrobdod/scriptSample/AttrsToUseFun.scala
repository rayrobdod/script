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
		def apply[A](funs:Iterable[Function1[A, Boolean]]) = {
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
		def apply(a:A) = funs.forall{_(a)}
		
		override def toString = "PolyAnd(" + funs.toString + ")"
	}
}
