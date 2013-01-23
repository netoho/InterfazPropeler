package propeler

import scala.swing.Panel
import java.awt.Color
import java.awt.Rectangle

class Cuadros(val n:Int, val m:Int) extends Panel {
  
  var datos:Array[Array[Boolean]] = Array.fill(m,n)(false)
  var drag = false;
  var dx:Double = _
  var dy:Double = _
  var prevN:Int = _
  var prevM:Int = _
  
  background = new Color(247, 247, 249)
  
  def getBytesForArduino():Array[Array[Byte]] = {
    val bytes:Array[Array[Byte]] = Array.fill(m, 6)(0)
    var byte:Byte = 0;
    var pd:Array[Boolean] = Array.fill(8)(false)
    for(im <- 0 until m)
      for(np <- 0 until 3)
        for(ea <- 0 until 2){
          if(ea == 0) pd = datos(im).slice(np*16,8)
          else pd = datos(im).slice((np+1)*16-8,8)
          byte = arrayBooleanToByte(pd)
          bytes(im)(2*np+ea) = byte
        }
    bytes
  }
  
  private def arrayBooleanToByte(array: Array[Boolean]):Byte = {
    var retorno:Byte = 0
    for(ia <- 0 until 8){
      if(array(ia)){
    	  retorno = (retorno + Math.pow(2, ia)).toByte
      }
    }
    retorno
  }
  
  override def paintComponent(g: scala.swing.Graphics2D){
    super.paintComponent(g)
    g.setColor(Color.BLACK)
    dx = size.width.toDouble/m
    dy = size.height.toDouble/n
    for(x <- dx to size.width by dx){
//      g.drawLine(x.toInt, 0, x.toInt, size.height)
    }
    for(y <- dy to size.height by dy){
//      g.drawLine(0, y.toInt, size.width, y.toInt)
    }
    for(nr <- 0 until n){
      for(nc <- 0 until m){
        if(datos(nc)(nr)){
          g.fillRect((nc*dx).toInt, (nr*dy).toInt, dx.toInt, dy.toInt)
        }
      }
    }
  }
}