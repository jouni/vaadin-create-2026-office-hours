# Vaadin Create 2026 Office Hours

A collection of small layout and styling prototypes I implemented during my "office hours" session at the Vaadin Create 2026 conference.

[![Watch the preview](https://github.com/jouni/vaadin-create-2026-office-hours/raw/refs/heads/main/preview.png)](https://github.com/jouni/vaadin-create-2026-office-hours/raw/refs/heads/main/preview.mov)

## Prototypes

- **Navigation rail toggle.** A customized `AppLayout` drawer that can be toggled between a full navigation drawer and a compact navigation rail.
- **Continuous scrolling into a lazy-loaded grid.** A typical data listing view with a summary area above the data table. The prototype allows a seemingly infinitely long grid to be the last component in the view while retaining lazy loading. Scrolling appears to continue through the view, but the scrolling region switches to the grid when the end of the surrounding view is reached. This works around `vaadin-grid`'s limitation that it cannot virtualize rows based on an external scrolling region.
- **iOS-style master-detail transition.** A customized `MasterDetailLayout` transition for viewports narrower than 500px. Opening the detail makes it take up the full width while the master area slides and fades out, mimicking iOS view navigation.

## Running the Demo

Run the `Application` class from your IDE, or start the application from the command line:

```bash
./mvnw
```

Then open http://localhost:8080.
