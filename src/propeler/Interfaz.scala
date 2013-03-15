package propeler

import scala.swing._
import scala.swing.event.ButtonClicked
import scala.swing.BorderPanel.Position._
import java.awt.Color
import scala.swing.event._
import java.nio.ByteBuffer
import java.io.File
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.awt.RenderingHints

object Interfaz extends SimpleSwingApplication {
  var ps: PuertoSerie = _
  def top = new MainFrame {
    title = "Propeller"
    preferredSize = new Dimension(660, 256)
    val conectar = new Button { text = "Conectar" }
    val borrar = new Button { text = "Limpiar" }
    val enviar = new Button { text = "Enviar Datos" }
    val cargarImg = new Button { text = "Cargar Archivo" }
    val label = new Label { text = "see el texto" }
    val cuadros = new Cuadros(48, 240) {
      preferredSize = new Dimension(660, 256)
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
        contents += cargarImg
      }) = South
      layout(new FlowPanel {
        contents += label
      }) = North
    }
    listenTo(conectar)
    listenTo(borrar)
    listenTo(enviar)
    listenTo(cargarImg)
    reactions += {
      case ButtonClicked(b) => {
        if (b.equals(conectar)) {
          if (ps != null && ps.conectado) {
            ps.desconectar()
            conectar.text = "Conectar"
            label.text = "Puerto serie desconectado"
          } else {
            // Puerto para el bluetooth (para el bluetooth siempre debe ser 115200 el baudrate.
            ps = new PuertoSerie("/dev/tty.linvor-DevB", 9600)
            // Puerto para el cable en el puerto usb que esta junto al lector de tarjetas
            //             ps = new PuertoSerie("/dev/tty.usbmodemfa131",115200)
            ps.conectar()
            if (ps.conectado) {
              label.text = "Puerto serie conectado."
              conectar.text = "Desconectar."
            } else label.text = "No se pudo conectar al puerto serie especificado."
          }
        } else if (b.equals(borrar)) {
          cuadros.datos = Array.fill(cuadros.m, cuadros.n)(false)
          repaint
        } else if (b.equals(enviar)) {
          println("see enviando")
          ps.escribirDatos(cuadros.getBytesForArduino())
        } else if (b.equals(cargarImg)) {
          val file = new FileChooser(new File("."))
          val result = file.showOpenDialog(null)
          if (result == FileChooser.Result.Approve) {
            println(file.selectedFile.getAbsolutePath())
            val image = ImageIO.read(file.selectedFile)
            println("height: " + image.getHeight() + " width: " + image.getWidth())
            val newImage = new BufferedImage(240, 48, BufferedImage.TYPE_BYTE_BINARY)
            val g = newImage.createGraphics()
            try {
              g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC)
              g.setBackground(background)
              g.clearRect(0, 0, 240, 48)
              g.drawImage(image, 0, 0, 240, 48, null)
            } catch {
              case ex: Exception => println(ex)
            }
            val salida = new File("/Users/netoho/gg.png")
            ImageIO.write(newImage, "png", salida)
            val bytes: Array[Array[Int]] = Array.fill(240, 6)(0)
            var byte = 0
            for (col <- 0 until 240) {
              val array = Array.fill(6)(0)
              byte = 0
              for (row <- 7 to 0 by -1) {
//                println(newImage.getRaster().getSample(col, row, 0))
                if (newImage.getRaster().getSample(col, row, 0) > 0)
                  byte = (byte + Math.pow(2, -1 * (row - 7))).toInt
                array(0) = byte
              }
              byte = 0
              for (row <- 15 to 8 by -1) {
                println(newImage.getRaster().getSample(col, row, 0))
                if (newImage.getRaster().getSample(col, row, 0) > 0)
                  byte = (byte + Math.pow(2, -1 * (row - 7))).toInt
                array(1) = byte
              }
              byte = 0
              for (row <- 23 to 15 by -1) {
                println(newImage.getRaster().getSample(col, row, 0))
                if (newImage.getRaster().getSample(col, row, 0) > 0)
                  byte = (byte + Math.pow(2, -1 * (row - 7))).toInt
                array(2) = byte
              }
              byte = 0
              for (row <- 31 to 24 by -1) {
                println(newImage.getRaster().getSample(col, row, 0))
                if (newImage.getRaster().getSample(col, row, 0) > 0)
                  byte = (byte + Math.pow(2, -1 * (row - 7))).toInt
                array(3) = byte
              }
              byte = 0
              for (row <- 39 to 32 by -1) {
                println(newImage.getRaster().getSample(col, row, 0))
                if (newImage.getRaster().getSample(col, row, 0) > 0)
                  byte = (byte + Math.pow(2, -1 * (row - 7))).toInt
                array(4) = byte
              }
              byte = 0
              for (row <- 47 to 40 by -1) {
                println(newImage.getRaster().getSample(col, row, 0))
                if (newImage.getRaster().getSample(col, row, 0) > 0)
                  byte = (byte + Math.pow(2, -1 * (row - 7))).toInt
                array(5) = byte
              }
              bytes(col) = array
            }
            bytes.foreach(row => println(row.mkString(",")))
            ps.escribirDatos(bytes)
          }
        }
      }
    }
  }
}