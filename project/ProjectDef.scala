import sbt._

object ProjectDef {

  def inDirectory(_name: String) =
    Project(_name, file(_name))

}
