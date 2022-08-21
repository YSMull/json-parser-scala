sealed trait Json

case class JNumber(value: Number) extends Json

case class JString(value: String) extends Json

sealed trait JBool extends Json

case object JTrue extends JBool

case object JFalse extends JBool

case object JNull extends Json

case class JArray(elements: Vector[Json]) extends Json

case class JObject(keyValues: Vector[(String, Json)]) extends Json