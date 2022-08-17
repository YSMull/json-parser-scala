import Token.TokenType

class Parser(var input: Lexer) {
  private var lookahead: Token = input.nextToken()

  private def consume(): Unit = {
    lookahead = input.nextToken
  }

  private def matchToken(tokenType: TokenType): Unit = {
    if (lookahead.tokenType eq tokenType) consume()
    else throw new RuntimeException("expecting " + tokenType + "; found " + lookahead)
  }

  private def jNumber(): Json = {
    if (lookahead.tokenType eq TokenType.NUMBER) {
      val number = lookahead.value.asInstanceOf[Number]
      consume()
      JNumber(number)
    } else {
      throw new RuntimeException("jNumber error, found: " + lookahead)
    }
  }

  private def jNull(): Json = {
    if (lookahead.tokenType eq TokenType.NULL) {
      consume()
      JNull
    } else {
      throw new RuntimeException("jNull error, found: " + lookahead)
    }
  }

  private def jBool(): Json = {
    if (lookahead.tokenType eq TokenType.BOOL) {
      val boolVal = lookahead.value.asInstanceOf[Boolean]
      consume()
      if (boolVal) JTrue else JFalse
    } else {
      throw new RuntimeException("jBool error, found: " + lookahead)
    }
  }

  private def jString(): Json = {
    if (lookahead.tokenType eq TokenType.STRING) {
      val strVal = lookahead.value.asInstanceOf[String]
      consume()
      JString(strVal)
    } else {
      throw new RuntimeException("jString error, found: " + lookahead)
    }
  }

  private def jObject(): Json = {
    matchToken(TokenType.L_CURLY_BRACKET)
    var keyValues = List[(String, Json)]()
    var done = false
    while ((lookahead.tokenType eq TokenType.STRING) && !done) {
      val key = lookahead.value.asInstanceOf[String]
      matchToken(TokenType.STRING)
      matchToken(TokenType.COLON)
      val value = json()
      keyValues = keyValues :+ (key, value)
      if (lookahead.tokenType eq TokenType.COMMA) {
        consume()
      } else {
        done = true
      }
    }
    matchToken(TokenType.R_CURLY_BRACKET)
    JObject(keyValues)
  }

  private def jArray(): Json = {
    matchToken(TokenType.L_SQUARE_BRACKET)
    var elements = List[Json]()
    var done = false
    while (!(lookahead.tokenType eq TokenType.R_SQUARE_BRACKET) && !done) {
      elements = elements :+ json()
      if (lookahead.tokenType eq TokenType.COMMA) {
        consume()
      } else {
        done = true
      }
    }
    matchToken(TokenType.R_SQUARE_BRACKET)
    JArray(elements)
  }

  def json(): Json = {
    lookahead.tokenType match {
      case TokenType.L_CURLY_BRACKET => jObject()
      case TokenType.L_SQUARE_BRACKET => jArray()
      case TokenType.NUMBER => jNumber()
      case TokenType.STRING => jString()
      case TokenType.BOOL => jBool()
      case TokenType.NULL => jNull()
      case _ => throw new RuntimeException("invalid tokenType: " + lookahead.tokenType)
    }
  }
}
