
package com.github.uisso.akka.labs.sandbox

import org.junit.runner.RunWith
import akka.actor.ActorSystem
import org.scalatest.FeatureSpec
import akka.actor.Inbox
import akka.actor.Props
import org.scalatest.junit.JUnitRunner
import akka.actor.ActorRef
import scala.concurrent.duration._

/**
 *
 * @author wilson
 *
 */
@RunWith(classOf[JUnitRunner])
class HelloAkkaScalaTest extends FeatureSpec {

  feature("Hello Akka Scala Test") {

    // Create the 'helloakka' actor system
    val system = ActorSystem("helloakka")

    // Create the 'greeter' actor
    val greeter = system.actorOf(Props[Greeter], "greeter")

    // Create an "actor-in-a-box"
    val inbox = Inbox.create(system)

    // Tell the 'greeter' to change its 'greeting' message
    greeter.tell(WhoToGreet("akka"), ActorRef.noSender)

    // Ask the 'greeter for the latest 'greeting'
    // Reply should go to the "actor-in-a-box"
    inbox.send(greeter, Greet)

    // Wait 5 seconds for the reply with the 'greeting' message
    val Greeting(message1) = inbox.receive(5.seconds)
    println(s"Greeting: $message1")

    // Change the greeting and ask for it again
    greeter.tell(WhoToGreet("typesafe"), ActorRef.noSender)
    inbox.send(greeter, Greet)
    val Greeting(message2) = inbox.receive(5.seconds)
    println(s"Greeting: $message2")

    val greetPrinter = system.actorOf(Props[GreetPrinter])
    // after zero seconds, send a Greet message every second to the greeter with a sender of the greetPrinter
    system.scheduler.schedule(0.seconds, 1.second, greeter, Greet)(system.dispatcher, greetPrinter)

  }

}