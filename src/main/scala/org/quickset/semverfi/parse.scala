package org.quickset.semverfi

import scala.util.parsing.combinator.RegexParsers

https://gist.github.com/MasseGuillaume/a68ad153f063956f48eedd5046483916


class Parse extends RegexParsers {

  private val Dot = "."

  private val Plus = "+"

  private val Dash = "-"

  private val SemverTag = "v"

  val id: Parser[String] = """[0-9A-Za-z-]+""".r

  val int: Parser[String] = """\d+""".r

  val anything: Parser[String] = """[0-9A-Za-z\.\-\_\+]+""".r

  def version: Parser[Valid] =
    latestVersion | buildVersion | preReleaseVersion | normalVersion

  def buildVersion: Parser[BuildVersion] =
    versionTuple ~ classifier.? ~ (Plus ~> ids) ^^ {
      case ((major, minor, patch) ~ maybeClassifier ~ build) ⇒
        BuildVersion(major, minor, patch,
          maybeClassifier.getOrElse(Nil),
          build)
    }

  def preReleaseVersion: Parser[PreReleaseVersion] =
    versionTuple ~ classifier ^^ {
      case ((major, minor, patch) ~ classifier) ⇒
        PreReleaseVersion(major, minor, patch,
          classifier)
    }

  def normalVersion: Parser[NormalVersion] =
    versionTuple ^^ {
      case (major, minor, patch) ⇒
        NormalVersion(major, minor, patch)
    }

  def versionTuple: Parser[(Int, Int, Int)] =
    SemverTag.? ~> int ~ (Dot ~> int) ~ (Dot ~> int) ^^ {
      case (maj ~ min ~ pat) ⇒
        (maj.toInt, min.toInt, pat.toInt)
    } | SemverTag.? ~> int ~ (Dot ~> int) ^^ {
      case (maj ~ min) ⇒
        (maj.toInt, min.toInt, 0)
    } | SemverTag.? ~> int ^^ {
      case (maj) ⇒
        (maj.toInt, 0, 0)
    }

  def ids: Parser[Seq[String]] =
    rep1(id | Dot ~> id)

  def classifier: Parser[Seq[String]] =
    Dash ~> ids

  def latestVersion: Parser[Latest] = {
    "(?i)^latest$".r ^^ { a => Latest() }
  }

  def apply(in: String) = try {
    parseAll(latestVersion | buildVersion | preReleaseVersion | normalVersion, in) match {
      case success if (success.successful) ⇒ success.get
      case failure ⇒ Invalid(in)
    }
  } catch {
    case e: NullPointerException ⇒ Invalid(in)
  }
}

object Parse {
  def apply(in: String): SemVersion = new Parse()(in)
}
