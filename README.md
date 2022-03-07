Point of sale system, developed with Java.
“Handle that POS” is a point of sale system created primarily for executing sales transactions and storing them in a database. This project aims to be easy to use and affordable. If you want to use this system, you need an SQL database, which can be local or cloud.

Development team: Anna Raevskaia, Antti Taponen, Nicklas Sundell, Samu Luoma, Lassi Piispanen

Maven dependencies and plugins:
- At.favre.lib
    - Bcrypt (0.9.0), https://github.com/patrickfav/bcrypt
- Org.apache.maven.plugins
    - Maven-surefire-plugin (3.0.0-M5)
    - Maven-compiler-plugin (3.8.1)
- Org.junit.jupiter
    - Junit-jupiter-params (5.7.2)
    - Junit-jupiter-api (5.7.2)
- Mysql
    - Mysql-connector-java (8.0.28)
- Org.hibernate
    - Hibernate-core (5.6.5 Final)
- Org.controlsfx
    - Controlsfx (11.1.1)
- Org.openjfx
    - Javafx-controls (17.0.2)
    - Javafx-fxml (17.0.2)
    - Javafx-controls (17.0.2)
- Com.dlsc.formsfx
    - Formsfx-core (11.4.2)
    
Installation and configuration:

To use this program, you have to create an SQL database named “pos” and construct the tables into it with the following script:

```
CREATE TABLE Tuote
(
  ID VARCHAR(8) NOT NULL,
  Varastomäärä INT NOT NULL,
  Hinta INT NOT NULL,
  Nimi VARCHAR(60) NOT NULL,
  Kuvaus VARCHAR(255) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE Käyttäjä
(
  ID INT NOT NULL,
  Salasana VARCHAR(255) NOT NULL,
  Käyttäjätunnus VARCHAR(60) NOT NULL,
  Etunimi VARCHAR(60) NOT NULL,
  Sukunimi VARCHAR(60) NOT NULL,
  Aktiivisuus INT NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE Maksupääte
(
  ID INT NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE Asiakas
(
  ID INT NOT NULL,
  Asiakkuus INT,
  PRIMARY KEY (ID)
);

CREATE TABLE Käyttäjätaso
(
  ID INT NOT NULL,
  Nimi VARCHAR(60) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE Maksutapa
(
  ID INT NOT NULL,
  Nimi VARCHAR(60) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE Käyttöoikeus
(
  ID INT NOT NULL,
  Oikeusalku DATE NOT NULL,
  Oikeusloppu DATE,
  KäyttäjätasoID INT NOT NULL,
  KäyttäjäID INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (KäyttäjätasoID) REFERENCES Käyttäjätaso(ID),
  FOREIGN KEY (KäyttäjäID) REFERENCES Käyttäjä(ID)
);

CREATE TABLE Maksutapahtuma
(
  ID INT NOT NULL,
  Aikaleima DATE NOT NULL,
  AsiakasID INT,
  MaksupääteID INT NOT NULL,
  MaksutapaID INT NOT NULL,
  KäyttäjäID INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (AsiakasID) REFERENCES Asiakas(ID),
  FOREIGN KEY (MaksupääteID) REFERENCES Maksupääte(ID),
  FOREIGN KEY (MaksutapaID) REFERENCES Maksutapa(ID),
  FOREIGN KEY (KäyttäjäID) REFERENCES Käyttäjä(ID)
);

CREATE TABLE Tilaus
(
  ID INT NOT NULL,
  MaksutapahtumaID INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (MaksutapahtumaID) REFERENCES Maksutapahtuma(ID)
);

CREATE TABLE Sisältyy
(
  Määrä INT NOT NULL,
  TuoteID VARCHAR(8) NOT NULL,
  TilausID INT NOT NULL,
  PRIMARY KEY (TuoteID, TilausID),
  FOREIGN KEY (TuoteID) REFERENCES Tuote(ID),
  FOREIGN KEY (TilausID) REFERENCES Tilaus(ID)
);

INSERT INTO `pos`.`Maksutapa` (`ID`, `Nimi`) VALUES ('0', 'CASH'),('1','CARD');
INSERT INTO `pos`.`Käyttäjätaso` (`ID`, `Nimi`) VALUES ('0', 'USER'),('1', 'MANAGER'),('2', 'ADMIN');
SET GLOBAL FOREIGN_KEY_CHECKS=0;
```

After constructing the database you have to edit JDBC’s connection settings in hibernate.cfg.xml (located in src/main/resources, you have to create the file also) to match your database connection information.
Code for hibernate config:

```
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- JDBC connection settings -->
        <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="connection.url"></property>
        <property name="connection.username"></property>
        <property name="connection.password" ></property>

        <!-- JDBC connection pool, use Hibernate internal connection pool -->
        <property name="connection.pool_size">5</property>

        <!-- Defines the SQL dialect used in Hibernate's application -->
        <property name="dialect">org.hibernate.dialect.MySQL8Dialect</property>

        <!-- Enable Hibernate's automatic session context management -->
        <property name="current_session_context_class">thread</property>

        <!-- Disable the second-level cache  -->
        <property name="cache.provider_class">org.hibernate.cache.internal.NoCachingRegionFactory</property>

        <!-- Display and format all executed SQL to stdout -->
        <property name="show_sql">true</property>
        <property name="format_sql">true</property>
        <property name="hibernate.connection.CharSet">utf8</property>
        <property name="hibernate.connection.characterEncoding">utf8</property>
        <property name="hibernate.connection.useUnicode">true</property>
        <!-- Drop and re-create the database schema on startup -->
        <property name="hbm2ddl.auto">update</property>

        <!-- Mapping to hibernate mapping files -->
        <mapping class="model.classes.User" />
        <mapping class="model.classes.Product" />
        <mapping class="model.classes.Order" />
        <mapping class="model.classes.OrderProduct" />
        <mapping class="model.classes.Customer" />
        <mapping class="model.classes.Transaction" />
        <mapping class="model.classes.POSEngine" />
        <mapping class="model.classes.Privilege" />
    </session-factory>
</hibernate-configuration>

```

The program itself is now set up but you have to insert users and products into the database in order to use the program at its full potential.


Manual for basic actions:

Step 1: Logging in
At first when launching the program, the user is prompted with a login screen. Log in with the credentials stored in the database after which the user is greeted with the main view.

Step 2: Adding products
At the bottom left corner of the main view there is a button “Asetukset” denoted by a cog icon. Click it and select the “Tuotteiden hallinta” tab. At the top of the panel on the right side of the screen click the “Lisää tuote” button and enter the product details and finally select “Tallenna” when you are ready with the product. “Tuotteen viivakoodi” is the barcode value and acts as a unique identifier for the database so make sure it is indeed unique.

Step 3: Scanning products
At the main view, there’s an input field near the bottom where you can input barcode values. After successful insert, the product should appear on the list. After the product is shown on the list you can click “+” or “-” buttons to edit the amount of the product in order.

Step 4: Completing transaction
After scanning products, the user can go to the transaction view by clicking “Maksu”. There they can click “Vahvista” and the transaction gets inserted to the database.

Extra: Printing receipt
If the user desires, they can print receipt of the transaction by going to the transaction view and checkmarking the box “kuitin tulostus”. After clicking “Vahvista” the user is prompted by the operating system printer manager, where they can choose what printer they want to use.

Extra: Choosing between payment methods
When the user is in the transaction view, they can choose a payment method by clicking either “Maksukortti” (payment card) or “Käteinen” (cash). Default payment method is Maksukortti (payment card).

Extra: Using hotkey buttons
To link a product with a hotkey button, first scan the desired product and then click and hold the button you want to configure for two seconds. After clicking and holding for two seconds the program asks if you want to name the button or use the name of the product from the database. After clicking “Ok” the hotkey button is set.

Extra: Editing product
To edit an existing product go to “Asetukset” and either go to “Tuotteiden hallinta” and select “Muokkaa tuotetta” or go to “Tuotteiden haku” and search and select a product you want to edit by double clicking it. In “Muokkaa tuotetta” -view input the ID of a product you want to edit and click “Hae” which fills the fields with current information. Input the new information for the product and click “Muokkaa”.

Extra: Deleting product from database
To delete a product from database go to “Asetukset”,  “Tuotteiden hallinta” and select “Poista tuote”. At “Poista tuote” -view input the ID of a product you want to delete and click “Poista”.
