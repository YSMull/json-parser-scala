import java.nio.file.{Files, Path}

object Main {
  def minify(json: Json): String = {
    json match {
      case JArray(elements) => "[" + elements.map(minify).mkString(",") + "]"
      case JObject(keyValues) => "{" + keyValues.map(kv => s"\"${kv._1}\":${minify(kv._2)}").mkString(",") + "}"
      case JNumber(number) => number.toString
      case JString(str) => s"\"${str}\""
      case JTrue => "true"
      case JFalse => "false"
      case JNull => "null"
    }
  }

  // git clone https://github.com/miloyip/nativejson-benchmark.git
  def parse(filename: String): Json = {
    val json: String = Files.readString(Path.of(s"/Users/ysmull/nativejson-benchmark/data/$filename"))
    val lexer = new Lexer(json)
    val parser = new Parser(lexer)
    parser.json()
  }

  def parseTest(): List[Json] = {
    val t0 = System.nanoTime()
    val asts = List("twitter.json", "citm_catalog.json", "canada.json").map(parse)
    val t1 = System.nanoTime()
    println("parse: " + (t1 - t0) / 1000000.0 + "ms")
    asts
  }

  def stringify(asts: List[Json]): Unit = {
    val t0 = System.nanoTime()
    asts.map(minify)
    val t1 = System.nanoTime()
    println("stringify: " + (t1 - t0) / 1000000.0 + "ms")
  }

  def main(args: Array[String]): Unit = {

    parseTest()
    parseTest()
    parseTest()
    parseTest()
    parseTest()
    parseTest()
    parseTest()
    parseTest()
    parseTest()
    parseTest()
    parseTest()
    parseTest()
    parseTest()
    parseTest()
    val asts = parseTest()
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)
    stringify(asts)

    //    System.out.println(minify(jsonAst))
  }
}