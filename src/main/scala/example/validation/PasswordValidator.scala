package example.validation

object PasswordValidator {

  def validate(password: String): ValidationResult[String] = {
    if (isPasswordValid(password)) {
      password.validNel
    } else {
      ("password", s"password: $password doesn't match creteria").invalidNel
    }
  }

  private def isPasswordValid(password: String): Boolean = {
    password.trim.nonEmpty &&
    password.exists(_.isUpper) &&
    password.exists(_.isLower) &&
    password.exists(_.isDigit) &&
    password.exists(!_.isLetterOrDigit) &&
    password.length >= 8 &&
    password.length <= 30
  }

}
