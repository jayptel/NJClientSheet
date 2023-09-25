package com.rhythm.njclienttsheet.models;

public class Item {

        private String name;
        private String description;
        private String age;
        // Constructor, setters, and other methods...

        public String getName() {
                return name;
        }

        public String getDescription() {
                return description;
        }

        public String getAge() {
                return age;
        }
        public Item(String name, String description, String age) {
                this.name = name;
                this.description = description;
                this.age = age;
        }
}
