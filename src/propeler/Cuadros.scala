package propeler

import scala.swing.Panel
import java.awt.Color
import java.awt.Rectangle

class Cuadros(val n: Int, val m: Int) extends Panel {

  var datos: Array[Array[Boolean]] = Array.fill(m, n)(true)
  var drag = false;
  var dx: Double = _
  var dy: Double = _
  var prevN: Int = _
  var prevM: Int = _

  background = new Color(247, 247, 249)

  def getBytesForArduino(): Array[Array[Int]] = {
    val bytes: Array[Array[Int]] = Array.fill(m, 6)(0)
    var byte: Int = 0;
    var pd: Array[Boolean] = Array.fill(8)(false)
    for (im <- 0 until m)
      for (np <- 0 until 6) {
        pd = datos(im).slice(np * 8, np * 8 + 8).reverse
        byte = arrayBooleanToByte(pd)
        bytes(im)(np) = byte
//        bytes(im)(np) = im
      }
    bytes.foreach(row => println(row.mkString(",")))
    bytes
  }

  private def arrayBooleanToByte(array: Array[Boolean]): Int = {
    var retorno: Int = 0
    for (ia <- 0 until 8) {
      if (array(ia)) {
        retorno = (retorno + Math.pow(2, ia)).toInt
      }
    }
    retorno
  }

  override def paintComponent(g: scala.swing.Graphics2D) {
    super.paintComponent(g)
    g.setColor(Color.BLACK)
    dx = size.width.toDouble / m
    dy = size.height.toDouble / n
    for (x <- dx to size.width by dx) {
      g.drawLine(x.toInt, 0, x.toInt, size.height)
    }
    for (y <- dy to size.height by dy) {
      g.drawLine(0, y.toInt, size.width, y.toInt)
    }
    for (nr <- 0 until n) {
      for (nc <- 0 until m) {
        if (datos(nc)(nr)) {
          g.fillRect((nc * dx).toInt, (nr * dy).toInt, dx.toInt, dy.toInt)
        }
      }
    }
  }
}