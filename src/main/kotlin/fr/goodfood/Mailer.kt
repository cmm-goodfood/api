package fr.goodfood

import fr.goodfood.entities.User
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import java.io.BufferedReader

import java.io.InputStreamReader
import java.nio.charset.StandardCharsets


object Mailer {
    fun send(user: User, subject: String, template: String, variables: HashMap<String, String> = HashMap()) {
        var content = readTemplate(template)
        for ((variable, value) in variables.entries) {
            content = content.replace(variable, value)
        }

        val email = EmailBuilder.startingBlank()
            .from("goodfood", "contact@goodfood.fr")
            .to(user.firstname + " " + user.lastname, user.email)
            .withSubject(subject)
            .withHTMLText(content)
            .buildEmail()

        val mailer = MailerBuilder
            .withSMTPServer(
                System.getenv("SMTP_HOST"),
                System.getenv("SMTP_PORT").toInt(),
                System.getenv("SMTP_USER"),
                System.getenv("SMTP_PASS")
            )
            .buildMailer()

        mailer.sendMail(email)
    }

    private fun readTemplate(path: String): String {
        return Mailer.javaClass.classLoader.getResource(path)!!.readText(StandardCharsets.UTF_8)
    }
}