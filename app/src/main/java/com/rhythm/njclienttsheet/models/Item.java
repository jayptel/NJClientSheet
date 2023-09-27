package com.rhythm.njclienttsheet.models;
import java.io.Serializable;
public class Item implements Serializable{

        private String name;
        private String description;
        private String age;
        // Constructor, setters, and other methods...
        private int nameColorRes;
        private int descriptionColorRes;
        private int ageColorRes;
        public String getName() {
                return name;
        }

        public String getDescription() {
                return description;
        }

        public String getAge() {
                return age;
        }
        public int getNameColorRes() {
                return nameColorRes;
        }

        public int getDescriptionColorRes() {
                return descriptionColorRes;
        }

        public int getAgeColorRes() {
                return ageColorRes;
        }
        public Item(String name, String description, String age) {
                this.name = name;
                this.description = description;
                this.age = age;
        }

        public void setName(String editedName) {
        }

        public void setDescription(String editedDescription) {
        }

        public void setAge(String editedAge) {
        }

        private boolean isSelected; // Add this field

        public boolean isSelected() {
                return isSelected;
        }

        public void setSelected(boolean selected) {
                isSelected = selected;
        }
}
