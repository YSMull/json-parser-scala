sealed trait Json

case class JNumber(value: Number) extends Json

case class JString(value: String) extends Json

case class JBool(value: Boolean) extends Json

case class JNull() extends Json

case class JArray(elements: List[Json]) extends Json

case class JObject(keyValues: List[(String, Json)]) extends Json