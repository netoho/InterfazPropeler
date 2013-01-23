package propeler

import gnu.io.NRSerialPort
import java.io.InputStream
import java.io.OutputStream
import scala.util.control.Exception

class PuertoSerie(puerto: String, br: Int) {
  var sp = new NRSerialPort(puerto, br);
  var is: InputStream = _;
  var os: OutputStream = _;

  def conectar(): Boolean = {
    var conectado = false
    try {
      sp.connect();
      os = sp.getOutputStream()
      is = sp.getInputStream()
      conectado = true
    } catch {
      case ex: Exception => println(ex)
    }
    conectado
  }

  def escribirDatos(datos: Array[Array[Byte]]) {
    datos.foreach(row => {
      try {
        os.write(row)
      } catch {
        case e: Exception => println(e.getCause())
      }
    })
  }
}