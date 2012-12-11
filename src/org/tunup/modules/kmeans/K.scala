package org.tunup.modules.kmeans

import org.tunup.core.Parameter

class K(normalizedValue: Double) extends Parameter(normalizedValue) {
  
  def description: String = "Value of K"
  def value: Double = (normalizedValue * 100) intValue
}