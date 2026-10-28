package com.example.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.server.streams.DownloadHandler;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * A Button with an animated, state-driven icon and a rolling label.
 * Styles live in activation-button.css; all motion is CSS, driven by --state.
 *
 * Lifecycle: start() → WORKING, finish(true) → DONE → (after holdDone) RESTING.
 * finish(false) goes straight back to RESTING. Updates made from background
 * threads need @Push.
 */
@StyleSheet("activation-button.css") // src/main/resources/META-INF/resources/
public class ActivationButton extends Button {

    public enum State {
        RESTING, WORKING, DONE
    }

    private static final ScheduledExecutorService TIMER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "activation-button-timer");
        t.setDaemon(true);
        return t;
    });

    private final String[] labels;
    private State state = State.RESTING;
    private Duration minWorking = Duration.ofMillis(1200);
    private Duration holdDone = Duration.ofMillis(2200);
    private long startedAt;
    private int run; // ignores timers from a previous run

    public ActivationButton(ActivationIcon icon, String resting, String working, String done) {
        this(icon.create(), resting, working, done);
    }

    public ActivationButton(Component icon, String resting, String working, String done) {
        labels = new String[] { resting, working, done };
        addClassName("activation-button");

        icon.getElement().setAttribute("slot", "prefix");
        icon.getElement().setAttribute("aria-hidden", "true");
        getElement().appendChild(icon.getElement());

        Span reel = new Span(new Span(resting), new Span(working), new Span(done));
        reel.addClassName("ab-labels");
        reel.getElement().setAttribute("aria-hidden", "true");
        getElement().appendChild(reel.getElement());

        Span fill = new Span();
        fill.addClassName("ab-fill");
        fill.getElement().setAttribute("slot", "suffix");
        fill.getElement().setAttribute("aria-hidden", "true");
        getElement().appendChild(fill.getElement());

        setState(State.RESTING);
    }

    // ---------------------------------------------------------------- state

    /** RESTING hands control back to CSS, so hover and focus work again. */
    public void setState(State state) {
        this.state = state;
        switch (state) {
            case RESTING -> getStyle().remove("--state");
            case WORKING -> getStyle().set("--state", "working");
            case DONE -> getStyle().set("--state", "done");
        }
        // ab-busy blocks clicks while a run is in progress (see the CSS)
        setClassName("ab-busy", state != State.RESTING);
        getElement().setAttribute("aria-label", labels[state.ordinal()]);
    }

    public State getState() {
        return state;
    }

    /**
     * Shortest time WORKING is shown, so a fast task still reads as an animation.
     */
    public void setMinWorking(Duration minWorking) {
        this.minWorking = minWorking;
    }

    /** How long DONE stays before returning to RESTING. */
    public void setHoldDone(Duration holdDone) {
        this.holdDone = holdDone;
    }

    /** Enters WORKING. Returns false if a run is already in progress. */
    public boolean start() {
        if (state != State.RESTING)
            return false;
        run++;
        startedAt = System.nanoTime();
        setState(State.WORKING);
        return true;
    }

    /**
     * Ends the run: DONE (then RESTING) on success, RESTING right away on failure.
     */
    public void finish(boolean success) {
        if (state != State.WORKING)
            return;
        if (!success) {
            setState(State.RESTING);
            return;
        }

        long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
        long wait = Math.max(0, minWorking.toMillis() - elapsed);
        int thisRun = run;
        later(wait, () -> {
            if (run != thisRun)
                return;
            setState(State.DONE);
            later(holdDone.toMillis(), () -> {
                if (run == thisRun && state == State.DONE)
                    setState(State.RESTING);
            });
        });
    }

    /** Runs an async task with the full sequence. Needs @Push. */
    public void run(Supplier<CompletableFuture<?>> task) {
        if (!start())
            return;
        UI ui = UI.getCurrent();
        task.get().whenComplete((result, error) -> access(ui, () -> finish(error == null)));
    }

    // ------------------------------------------------------------ downloads

    /**
     * Clicking the button downloads what the handler serves.
     * A hidden link carries the download, so the browser saves the file natively
     * and the button stays a real button for keyboard and screen-reader users.
     * Hook the handler's whenStart/whenComplete to start()/finish() to animate.
     */
    public ActivationButton downloadOnClick(DownloadHandler handler) {
        Anchor link = new Anchor(handler, "");
        link.getElement().setAttribute("aria-hidden", "true");
        link.getElement().setAttribute("tabindex", "-1");
        link.getStyle().set("display", "none");
        getElement().appendChild(link.getElement());

        // Forward the click in the browser, inside the user's gesture.
        addAttachListener(e -> getElement().executeJs("""
                const link = $0;
                this.addEventListener('click', (event) => {
                  if (event.target === link || this.classList.contains('ab-busy')) return;
                  link.click();
                });""", link.getElement()));
        return this;
    }

    // ------------------------------------------------------------- helpers

    private void later(long millis, Runnable task) {
        UI ui = UI.getCurrent();
        if (ui == null)
            return;
        TIMER.schedule(() -> access(ui, task), millis, TimeUnit.MILLISECONDS);
    }

    private static void access(UI ui, Runnable task) {
        try {
            ui.access(task::run);
        } catch (UIDetachedException ignored) {
            // the user navigated away; nothing to update
        }
    }

    /** The animated icons that ship with activation-button.css. */
    public enum ActivationIcon {
        EXPORT("""
                <span class="ab-icon-export"><svg viewBox="0 0 24 24">
                  <defs><clipPath id="ex-clip"><rect x="-4" y="0" width="32" height="17"/></clipPath></defs>
                  <g clip-path="url(#ex-clip)">
                    <g class="ex-arrow"><path d="M12 3.5V15M7.5 10.5 12 15l4.5-4.5"/></g>
                    <g class="ex-check"><path d="M8.25 9.75l2.75 2.75 5-5.5"/></g>
                  </g>
                  <g class="ex-tray"><path d="M4 14.5V18a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-3.5"/></g>
                </svg></span>"""),
        ACTIVATE(
                """
                        <span class="ab-icon-activate"><svg viewBox="0 0 40 40">
                          <defs><clipPath id="act-clip"><circle cx="20" cy="20" r="20"/></clipPath></defs>
                          <g class="spin"><circle class="disc" cx="20" cy="20" pathLength="100" transform="rotate(-90 20 20)"/></g>
                          <g clip-path="url(#act-clip)">
                            <path class="glyph arrow" d="M20 27.5V12.5M14 18.5l6-6 6 6"/>
                            <path class="glyph check" d="M13.75 20.5l4.5 4.5 8-8.5"/>
                          </g>
                        </svg></span>""");

        private final String markup;

        ActivationIcon(String markup) {
            this.markup = markup;
        }

        public Component create() {
            return new Html(markup);
        }
    }
}
