package propeler

import scala.swing._
import scala.swing.event.ButtonClicked
import scala.swing.BorderPanel.Position._
import java.awt.Color
import scala.swing.event._
import java.nio.ByteBuffer

object Interfaz extends SimpleSwingApplication {
  var ps: PuertoSerie = _
  def top = new MainFrame {
    title = "Propeller"
    preferredSize = new Dimension(640, 256)
    val conectar = new Button { text = "Conectar" }
    val borrar = new Button { text = "Limpiar" }
    val enviar = new Button { text = "Enviar Datos" }
    val label = new Label { text = "see el texto" }
    val cuadros = new Cuadros(48, 2) {
      preferredSize = new Dimension(300, 300)
      listenTo(mouse.clicks)
      listenTo(mouse.moves)
      reactions += {
        case e: MousePressed => {
          drag = true;
          prevN = (e.point.y / dy).toInt
          prevM = (e.point.x / dx).toInt
        }
        case e: MouseReleased => {
          drag = false;
        }
        case e: MouseDragged => {
          if (drag && !((e.point.y / dy).toInt == prevN && (e.point.x / dx).toInt == prevM)) {
            //            datos((e.point.y/dy).toInt)((e.point.x/dx).toInt) = !datos((e.point.y/dy).toInt)((e.point.x/dx).toInt)
            datos((e.point.x / dx).toInt)((e.point.y / dy).toInt) = true;
            prevN = (e.point.y / dy).toInt
            prevM = (e.point.x / dx).toInt
            repaint
          }
        }
      }
    }
    contents = new BorderPanel {
      menuBar = new MenuBar {
        contents += new Menu("Archivo")
        contents += new Menu("Preferencias") {
          contents += new MenuItem(Action("Puerto Serial") {
          })
        }
      }
      layout(new BoxPanel(Orientation.Vertical) {
        contents += cuadros
      }) = Center
      layout(new FlowPanel {
        contents += conectar
        contents += borrar
        contents += enviar
      }) = South
      layout(new FlowPanel {
        contents += label
      }) = North
    }
    listenTo(conectar)
    listenTo(borrar)
    listenTo(enviar)
    reactions += {
      case ButtonClicked(b) => {
        if (b.equals(conectar)) {
          if (ps != null && ps.conectado) {
            ps.desconectar()
            conectar.text = "Conectar"
            label.text = "Puerto serie desconectado"
          } else {
            // Puerto para el bluetooth (para el bluetooth siempre debe ser 115200 el baudrate.
            ps = new PuertoSerie("/dev/tty.RN42-589A-SPP", 115200)
            // Puerto para el cable en el puerto usb que esta junto al lector de tarjetas
            // ps = new PuertoSerie("/dev/tty.usbmodemfd121",115200)
            ps.conectar()
            if (ps.conectado) {
              label.text = "Puerto serie conectado."
              conectar.text = "Desconectar"
            } else label.text = "No se pudo conectar al puerto serie especificado."
          }
        } else if (b.equals(borrar)) {
          cuadros.datos = Array.fill(cuadros.m, cuadros.n)(false)
          repaint
        } else if (b.equals(enviar)) {
          println("see enviando")
          ps.escribirDatos(cuadros.getBytesForArduino())
        }
      }
    }
  }
}