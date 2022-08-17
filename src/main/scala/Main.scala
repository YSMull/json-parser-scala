import java.nio.file.{Files, Path}

object Main {
  def minify(json: Json): String = {
    json match {
      case JArray(elements) => "[" + elements.map(minify).mkString(",") + "]"
      case JObject(keyValues) => "{" + keyValues.map(kv => s"\"${kv._1}\":${minify(kv._2)}").mkString(",") + "}"
      case JNumber(number) => number.toString
      case JString(str) => s"\"${str}\""
      case JBool(bool) => bool.toString
      case JNull() => "null"
    }
  }

  def main(args: Array[String]): Unit = {
    val json: String = Files.readString(Path.of("./test.json"))
//    val tokens: util.List[Token] = new Lexer(json).tokenize
    val lexer = new Lexer(json)
    val parser = new Parser(lexer)
    val jsonAst = parser.json()
    System.out.println(minify(jsonAst))
  }
}