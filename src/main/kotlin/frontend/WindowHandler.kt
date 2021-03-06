package frontend

import backend.Configuration
import backend.Logger
import frontend.screens.StartingScreen
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.WindowConstants

object WindowHandler {

    /**
     * Window of the complete frontend
     */
    lateinit var window: JFrame

    /**
     * Component of the window
     */
    private lateinit var component: JComponent

    /**
     * Current screen of the window
     */
    lateinit var screen: Screen

    /**
     * Changeable fps count of the window
     */
    var fps: Long = 60

    fun openWindow() {
        Thread {

            screen = StartingScreen

            component = object : JComponent() {
                override fun paint(graphics: Graphics?) {
                    if (graphics != null) {
                        (graphics as Graphics2D).setRenderingHint(
                            RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY
                        )
                        graphics.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                        )
                        graphics.setRenderingHint(
                            RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                        )
                        graphics.setRenderingHint(
                            RenderingHints.KEY_STROKE_CONTROL,
                            RenderingHints.VALUE_STROKE_NORMALIZE
                        )
                        graphics.setRenderingHint(
                            RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR
                        )
                        graphics.setRenderingHint(
                            RenderingHints.KEY_FRACTIONALMETRICS,
                            RenderingHints.VALUE_FRACTIONALMETRICS_ON
                        )

                        graphics.color = Color.WHITE
                        graphics.fillRect(0, 0, 800, 600)

                        screen.paint(graphics, graphics, this)
                    }
                }
            }

            window = JFrame()
            window.add(component)

            window.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

            window.addKeyListener(object : KeyListener {
                override fun keyTyped(e: KeyEvent?) {
                }

                override fun keyPressed(e: KeyEvent?) {
                }

                override fun keyReleased(e: KeyEvent?) {
                    if (e?.keyCode == 122) {
                        window.dispose()

                        if (window.isUndecorated) {
                            window.isUndecorated = false
                            window.isAlwaysOnTop = false
                            Logger.log("Switched to windowed mode", this.javaClass)
                        } else {
                            window.isUndecorated = true
                            window.isAlwaysOnTop = true
                            Logger.log("Switched to fullscreen mode", this.javaClass)
                        }
                        window.isVisible = true
                    }
                }

            })

            window.isUndecorated = Configuration.get("fullscreen") == "true"
            window.setSize(800, 600)
            window.isResizable = false
            window.isAlwaysOnTop = Configuration.get("fullscreen") == "true"
            window.title = "Hardware Monitoring Display | ${HardwareMonitoringDisplay.version} (800x600)"
            window.iconImage = ImageIO.read(File("files/images/WindowIcon.png"))

            window.location = Point(Configuration.get("posX").toInt(), Configuration.get("posY").toInt())

            window.isVisible = true

            // Adding custom shutdown hook
            Runtime.getRuntime().addShutdownHook(Thread {
                Logger.log("Killing OHM process...", this.javaClass)
                Runtime.getRuntime().exec("taskkill /IM \"OpenHardwareMonitor.exe\" /F")

                Configuration.set("posX", window.location.x.toString())
                Configuration.set("posY", window.location.y.toString())
                Configuration.set("fullscreen", window.isUndecorated.toString())
            })

            while (true) {
                Thread.sleep(1000 / fps)
                window.repaint()
            }
        }.start()
    }

    fun changeWindowSize(width: Int, height: Int) {
        window.dispose()

        window.setSize(width, height)
        window.title = "Hardware Monitoring Display | ${HardwareMonitoringDisplay.version} (${width}x${height})"

        window.isVisible = true
    }

}