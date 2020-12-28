package example.validation

object NameValidator {

  def validate(name: String, field: String = "name"): ValidationResult[String] = {
    if (name.length > 35) {
      (field, "{wrong_length}").invalidNel
    } else {
      name.validNel
    }
  }

}
