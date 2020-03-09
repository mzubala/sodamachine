package pl.com.bottega.sodamachine;

import io.vavr.control.Try;

interface Display {

    Try<Void> displayPermanently(String text);

    Try<Void> displayBriefly(String text);

    Try<Void> displayBrieflyAndThePermanently(String briefText, String permanentText);
}
