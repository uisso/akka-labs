package com.github.uisso.akka.labs.sandbox;

import java.io.Serializable;

import akka.actor.UntypedActor;

public class HelloAkkaJava {

    public static class Greet implements Serializable {

        private static final long serialVersionUID = 1L;
    }

    public static class WhoToGreet implements Serializable {

        private static final long serialVersionUID = 1L;
        public final String who;

        public WhoToGreet(String who) {
            this.who = who;
        }
    }

    public static class Greeting implements Serializable {

        private static final long serialVersionUID = 1L;
        public final String message;

        public Greeting(String message) {
            this.message = message;
        }
    }

    public static class Greeter extends UntypedActor {
        String greeting = "";

        @Override
        public void onReceive(Object message) {
            if (message instanceof WhoToGreet) {
                greeting = "hello, " + ((WhoToGreet) message).who;
            } else if (message instanceof Greet) {
                // Send the current greeting back to the sender
                getSender().tell(new Greeting(greeting), getSelf());
            } else {
                unhandled(message);
            }
        }
    }

    public static class GreetPrinter extends UntypedActor {
        @Override
        public void onReceive(Object message) {
            if (message instanceof Greeting) {
                System.out.println(((Greeting) message).message);
            }
        }
    }

}
