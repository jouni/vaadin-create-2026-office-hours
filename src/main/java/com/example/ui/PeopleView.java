package com.example.ui;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.vaadin.flow.function.SerializableBiConsumer;

import com.example.ui.ActivationButton.ActivationIcon;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayoutVariant;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

import static com.example.ui.CsvExport.column;

@ParentLayout(MainLayout.class)
@Route(value = "people", layout = MainLayout.class)
@PageTitle("People · My App")
// @StyleSheet("./people-view.css")
@CssImport(value = "./vaadin-grid.css", themeFor = "vaadin-grid")
public class PeopleView extends MasterDetailLayout {

    private static final List<Person> PEOPLE = List.of(
            new Person("Maya Chen", "maya.chen@example.com", "Product Design", "Barcelona, ES", "Active", "Sep 12, 2024", 1),
            new Person("Liam Foster", "liam.foster@example.com", "Engineering", "London, UK", "Active", "Jun 03, 2023", 2),
            new Person("Sofia Alvarez", "sofia.alvarez@example.com", "People Ops", "Madrid, ES", "On leave", "Jan 18, 2022", 3),
            new Person("Noah Williams", "noah.williams@example.com", "Marketing", "New York, US", "Active", "Nov 21, 2024", 4),
            new Person("Amara Okafor", "amara.okafor@example.com", "Finance", "Remote", "Active", "Mar 07, 2021", 5),
            new Person("Hugo Martin", "hugo.martin@example.com", "Engineering", "Paris, FR", "Invited", "Oct 25, 2026", 6),
            new Person("Elena Rossi", "elena.rossi@example.com", "Product Design", "Milan, IT", "Active", "Apr 14, 2023", 1),
            new Person("Kenji Sato", "kenji.sato@example.com", "Engineering", "Tokyo, JP", "Active", "Aug 28, 2022", 2),
            new Person("Priya Nair", "priya.nair@example.com", "Finance", "Bengaluru, IN", "On leave", "Feb 05, 2024", 3),
            new Person("Lucas Silva", "lucas.silva@example.com", "Marketing", "Lisbon, PT", "Active", "Jul 19, 2021", 4),
            new Person("Freja Nielsen", "freja.nielsen@example.com", "People Ops", "Copenhagen, DK", "Active", "Dec 01, 2023", 5),
            new Person("Daniel Kim", "daniel.kim@example.com", "Engineering", "Seoul, KR", "Invited", "Oct 27, 2026", 6),
            new Person("Aisha Rahman", "aisha.rahman@example.com", "Product Design", "Amsterdam, NL", "Active", "May 16, 2022", 1),
            new Person("Mateo Garcia", "mateo.garcia@example.com", "Finance", "Mexico City, MX", "Active", "Jan 09, 2023", 2),
            new Person("Zoe Laurent", "zoe.laurent@example.com", "Marketing", "Lyon, FR", "On leave", "Sep 30, 2024", 3),
            new Person("Samuel Adeyemi", "samuel.adeyemi@example.com", "Engineering", "Remote", "Active", "Mar 22, 2022", 4),
            new Person("Ingrid Berg", "ingrid.berg@example.com", "People Ops", "Stockholm, SE", "Active", "Jun 11, 2023", 5),
            new Person("Ethan Brooks", "ethan.brooks@example.com", "Product Design", "Toronto, CA", "Active", "Nov 04, 2021", 6),
            new Person("Leila Haddad", "leila.haddad@example.com", "Marketing", "Dubai, AE", "Invited", "Oct 26, 2026", 1),
            new Person("Tomas Novak", "tomas.novak@example.com", "Engineering", "Prague, CZ", "Active", "Aug 07, 2020", 2),
            new Person("Nia Thompson", "nia.thompson@example.com", "Finance", "Chicago, US", "Active", "Feb 18, 2023", 3),
            new Person("Oliver Schmidt", "oliver.schmidt@example.com", "Engineering", "Berlin, DE", "On leave", "Oct 13, 2022", 4),
            new Person("Carla Mendes", "carla.mendes@example.com", "People Ops", "Porto, PT", "Active", "May 29, 2024", 5),
            new Person("Yuki Tanaka", "yuki.tanaka@example.com", "Product Design", "Osaka, JP", "Active", "Jul 02, 2023", 6),
            new Person("Benjamin Wilson", "benjamin.wilson@example.com", "Marketing", "Sydney, AU", "Active", "Dec 15, 2021", 1),
            new Person("Fatima Zahra", "fatima.zahra@example.com", "Finance", "Casablanca, MA", "Invited", "Oct 28, 2026", 2),
            new Person("Marco Bianchi", "marco.bianchi@example.com", "Engineering", "Rome, IT", "Active", "Apr 03, 2020", 3),
            new Person("Ana Petrovic", "ana.petrovic@example.com", "People Ops", "Belgrade, RS", "Active", "Jan 24, 2024", 4),
            new Person("Dario Costa", "dario.costa@example.com", "Marketing", "Sao Paulo, BR", "On leave", "Sep 08, 2022", 5),
            new Person("Grace Lee", "grace.lee@example.com", "Product Design", "Vancouver, CA", "Active", "Jun 20, 2021", 6));

    private static final List<CsvExport.Column<Person>> CSV_COLUMNS = List.of(
            column("Name", Person::name),
            column("Email", Person::email),
            column("Location", Person::location),
            column("Status", Person::status));

    public PeopleView() {
        addClassName("people-view");
        setSizeFull();
        // setDetailSize("420px", "min(420px, 100vw)");
        setExpandMaster(true);
        setMaster(createPageContent());

        addBackdropClickListener(event -> UI.getCurrentOrThrow().navigate(PeopleView.class));
        addDetailEscapePressListener(event -> UI.getCurrentOrThrow().navigate(PeopleView.class));
    }

    private Component createPageContent() {
        var content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(false);
        content.setSpacing(false);
        content.add(new ViewHeader(), createPageHeading(), createMetrics(), createDirectoryHeader(), createFilterBar(), createPeopleGrid());

        var scroller = new Scroller(content);
        scroller.setSizeFull();
        scroller.addClassName("people-content");
        return scroller;
    }

    private Component createPageHeading() {
        var title = new H1("People");
        var subtitle = new Paragraph("A clear view of everyone across your organization.");

        var copy = new VerticalLayout(title, subtitle);
        copy.addClassName("people-heading-copy");
        copy.setPadding(false);
        copy.setSpacing(false);

        var addPerson = new Button("Add person", LucideIcon.USER_PLUS.create());
        addPerson.addThemeVariants(ButtonVariant.PRIMARY);

        var heading = new HorizontalLayout(copy, addPerson);
        heading.addClassName("people-heading");
        heading.setAlignItems(HorizontalLayout.Alignment.CENTER);
        heading.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        heading.setWidthFull();
        return heading;
    }

    private Component createMetrics() {
        var metrics = new HorizontalLayout(
                metricCard("Total people", "248", "+12 this quarter", "people", "blue"),
                metricCard("New this month", "14", "+5 vs last month", "new", "violet"),
                metricCard("On leave", "8", "3 returning soon", "leave", "amber"),
                metricCard("Remote today", "63%", "156 people", "remote", "mint"));
        metrics.setWidthFull();
        metrics.addThemeVariants(HorizontalLayoutVariant.WRAP);
        metrics.getElement().getStyle().set("flex-wrap", "balance");
        return metrics;
    }

    private Card metricCard(String label, String value, String detail, String type, String color) {
        var icon = switch (type) {
            case "new" -> LucideIcon.USER_PLUS.create();
            case "leave" -> LucideIcon.CALENDAR_DAYS.create();
            case "remote" -> LucideIcon.GLOBE.create();
            default -> LucideIcon.USERS.create();
        };
        icon.addClassName("metric-icon-svg");

        var iconBadge = new Badge(icon);
        iconBadge.addThemeVariants(BadgeVariant.ICON_ONLY);
        iconBadge.addClassNames("metric-icon", color);

        var labelText = new Span(label);
        labelText.addClassName("metric-label");
        var header = new HorizontalLayout(labelText, iconBadge);
        header.addClassName("metric-card-header");
        header.setAlignItems(HorizontalLayout.Alignment.CENTER);
        header.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        header.setWidthFull();

        var valueText = new Span(value);
        valueText.addClassName("metric-value");
        var detailText = new Span(detail);
        detailText.addClassName("metric-detail");

        var card = new Card();
        card.addThemeVariants(CardVariant.OUTLINED);
        card.setHeader(header);
        card.add(valueText, detailText);
        card.addClassName("metric-card");
        return card;
    }

    private Component createDirectoryHeader() {
        var title = new H2("People directory");
        var count = new Badge("248 people");
        count.addThemeVariants(BadgeVariant.SMALL);

        var titleLine = new HorizontalLayout(title, count);
        titleLine.addClassName("directory-title-line");
        titleLine.setAlignItems(HorizontalLayout.Alignment.CENTER);

        var intro = new Paragraph("Browse teammates, roles and work locations.");
        var copy = new VerticalLayout(titleLine, intro);
        copy.addClassName("directory-copy");
        copy.setPadding(false);
copy.setSpacing(false);

        var export = new ActivationButton(ActivationIcon.EXPORT,"Export", "Exporting","Exported");
        export.addClassName("secondary-action");
        export.downloadOnClick(DownloadHandler
        .fromInputStream(event -> {
            byte[] csv = CsvExport.toBytes(PEOPLE, CSV_COLUMNS);
            return new DownloadResponse(new ByteArrayInputStream(csv),
                    "people-" + LocalDate.now() + ".csv",
                    "text/csv; charset=UTF-8", csv.length);
        })
        .whenStart(export::start)
        .onProgress(new SerializableBiConsumer<Long, Long>() {

            @Override
            public void accept(Long t, Long u) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        })
        .whenComplete(export::finish));

        var header = new HorizontalLayout(copy, export);
        header.addClassName("directory-header");
        header.setAlignItems(HorizontalLayout.Alignment.CENTER);
        header.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        header.setWidthFull();
        return header;
    }

    private Component createFilterBar() {
        var search = new TextField();
        search.setPlaceholder("Search by name or email");
        search.setPrefixComponent(LucideIcon.SEARCH.create());
        search.setAriaLabel("Search people");
        search.addClassName("people-search");

        var filters = new HorizontalLayout(
                search,
                filterSelect("Filter by team", "All teams", "All teams", "Engineering", "Product Design", "Finance", "Marketing", "People Ops"),
                filterSelect("Filter by location", "Any location", "Any location", "Barcelona, ES", "London, UK", "Madrid, ES", "New York, US", "Paris, FR", "Remote"),
                filterSelect("Filter by status", "Any status", "Any status", "Active", "On leave", "Invited"));
        filters.addThemeVariants(HorizontalLayoutVariant.WRAP);

        var clear = new Button("Clear filters");
        clear.addClassName("clear-action");

        var filterBar = new HorizontalLayout(filters, clear);
        filterBar.addClassName("filter-bar");
        filterBar.setAlignItems(HorizontalLayout.Alignment.CENTER);
        filterBar.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        filterBar.setWidthFull();
        return filterBar;
    }

    private Select<String> filterSelect(String ariaLabel, String value, String... options) {
        var select = new Select<String>();
        select.setItems(options);
        select.setValue(value);
        select.setAriaLabel(ariaLabel);
        select.setNoVerticalOverlap(true);
        select.addClassName("filter-select");
        return select;
    }

    private Grid<Person> createPeopleGrid() {
        var grid = new Grid<Person>(Person.class, false);
        grid.addClassName("people-grid");
        grid.addThemeVariants(GridVariant.NO_BORDER);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setItems(PEOPLE);
        grid.getElement().setAttribute("aria-label", "People directory");
        grid.addItemClickListener(event -> UI.getCurrentOrThrow().navigate(
                PersonDetailView.class,
                new RouteParameters("personId", event.getItem().id())));

        grid.addComponentColumn(this::createPersonCell)
                .setHeader("Person")
                .setWidth("280px")
                .setFlexGrow(2);
        grid.addColumn(Person::team)
                .setHeader("Team")
                .setWidth("150px")
                .setFlexGrow(1);
        grid.addComponentColumn(this::createLocationCell)
                .setHeader("Location")
                .setWidth("170px")
                .setFlexGrow(1);
        grid.addComponentColumn(this::createStatusBadge)
                .setHeader("Status")
                .setWidth("120px")
                .setFlexGrow(0);
        grid.addColumn(Person::startDate)
                .setHeader("Start date")
                .setWidth("135px")
                .setFlexGrow(0);
        grid.addComponentColumn(this::createRowMenu)
                .setAutoWidth(true)
                .setFlexGrow(0);
        return grid;
    }

    private Component createPersonCell(Person person) {
        var avatar = new Avatar(person.name());
        avatar.setColorIndex(person.colorIndex());
        avatar.setTooltipEnabled(true);
        avatar.addThemeVariants(AvatarVariant.SMALL);

        var name = new Span(person.name());
        name.addClassName("person-name");
        var email = new Span(person.email());
        email.addClassName("person-email");

        var identity = new VerticalLayout(name, email);
        identity.addClassName("person-identity");
        identity.setPadding(false);
        identity.setSpacing(false);

        var cell = new HorizontalLayout(avatar, identity);
        cell.addClassName("person-cell");
        cell.setAlignItems(HorizontalLayout.Alignment.CENTER);
        return cell;
    }

    private Component createLocationCell(Person person) {
        var location = new HorizontalLayout(LucideIcon.MAP_PIN.create(), new Span(person.location()));
        location.addClassName("location-cell");
        location.setAlignItems(HorizontalLayout.Alignment.CENTER);
        return location;
    }

    private Component createStatusBadge(Person person) {
        var badge = new Badge(person.status());
        badge.addThemeVariants(BadgeVariant.SMALL);
        switch (person.status()) {
            case "Active" -> badge.addThemeVariants(BadgeVariant.SUCCESS);
            case "On leave" -> badge.addThemeVariants(BadgeVariant.WARNING);
            default -> { }
        }
        badge.addClassName("status-badge");
        return badge;
    }

    private Component createRowMenu(Person person) {
        var menu = new Button(LucideIcon.ELLIPSIS.create());
        menu.setAriaLabel("More options for " + person.name());
        menu.addClassName("row-menu");
        return menu;
    }

    static Optional<Person> findPersonById(String personId) {
        return PEOPLE.stream().filter(person -> person.id().equals(personId)).findFirst();
    }

    record Person(String name, String email, String team, String location,
            String status, String startDate, int colorIndex) {

        String id() {
            return email.substring(0, email.indexOf('@')).replace('.', '-');
        }
    }
}
