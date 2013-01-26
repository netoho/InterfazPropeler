package propeler

import gnu.io.NRSerialPort
import java.io.InputStream
import java.io.OutputStream
import scala.util.control.Exception

class PuertoSerie(puerto: String, br: Int) {
  var sp = new NRSerialPort(puerto, br);
  var is: InputStream = _;
  var os: OutputStream = _;
  var conectado = false;

  def conectar(): Unit = {
    conectado = false
    try {
      sp.connect()
      os = sp.getOutputStream()
      is = sp.getInputStream()
      conectado = true
    } catch {
      case ex: Exception => println(ex)
    }
  }

  def desconectar(): Unit = {
    conectado = true
    try {
      os.close()
      is.close()
      sp.disconnect()
      conectado = false
    } catch {
      case ex: Exception => println(ex)
    }
  }

  def escribirDatos(datos: Array[Array[Int]]) {
    datos.foreach(row => {
      row.foreach(value => {
        try {
          os.write(value)
        } catch {
          case e: Exception => println(e.getCause())
        }
      })
    })
  }
}