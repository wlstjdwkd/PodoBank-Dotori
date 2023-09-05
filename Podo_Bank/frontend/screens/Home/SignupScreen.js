import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  Button,
  StyleSheet,
  TouchableOpacity,
} from "react-native";
import { LinearGradient } from "expo-linear-gradient";
import DateTimePicker from "@react-native-community/datetimepicker";

export default function SignupScreen({ navigation }) {
  const [selectedGender, setSelectedGender] = useState(null);
  const [date, setDate] = useState(new Date());
  const [showDatePicker, setShowDatePicker] = useState(false);

  const onDateChange = (event, selectedDate) => {
    const currentDate = selectedDate || date;
    setShowDatePicker(false);
    setDate(currentDate);
  };

  return (
    <LinearGradient
      colors={["#6BB5FF", "white"]}
      style={signupStyles.container}
    >
      <TextInput placeholder="Name" style={signupStyles.input} />
      <TextInput placeholder="ID" style={signupStyles.input} />
      <TextInput
        placeholder="Password"
        secureTextEntry={true}
        style={signupStyles.input}
      />
      <TextInput
        placeholder="Confirm Password"
        secureTextEntry={true}
        style={signupStyles.input}
      />

      {/* 날짜 선택 */}
      {showDatePicker && (
        <DateTimePicker
          testID="dateTimePicker"
          value={date}
          mode={"date"}
          is24Hour={true}
          display="default"
          onChange={onDateChange}
        />
      )}
      <TouchableOpacity onPress={() => setShowDatePicker(true)}>
        <TextInput
          placeholder="Birthday (dd/mm/yyyy)"
          style={signupStyles.input}
          value={`${date.getDate()}/${
            date.getMonth() + 1
          }/${date.getFullYear()}`}
          editable={false}
        />
      </TouchableOpacity>

      {/* 성별 선택은 라디오 버튼 */}
      <View style={signupStyles.genderContainer}>
        <Text style={signupStyles.genderText}>Gender:</Text>
        <TouchableOpacity
          style={[
            selectedGender === "Male"
              ? signupStyles.radioOptionSelected
              : signupStyles.radioOption,
            signupStyles.radio,
          ]}
          onPress={() => setSelectedGender("Male")}
        >
          <Text style={signupStyles.radioText}>Male</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[
            selectedGender === "Female"
              ? signupStyles.radioOptionSelected
              : signupStyles.radioOption,
            signupStyles.radio,
          ]}
          onPress={() => setSelectedGender("Female")}
        >
          <Text style={signupStyles.radioText}>Female</Text>
        </TouchableOpacity>
      </View>

      <TextInput placeholder="Address" style={signupStyles.input} />

      <View style={signupStyles.buttonContainer}>
        <TouchableOpacity
          style={signupStyles.button}
          onPress={() => navigation.navigate("SignupCompleteScreen")}
        >
          <Text style={signupStyles.buttonText}>Sign Up</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={signupStyles.button}
          onPress={() => navigation.goBack()}
        >
          <Text style={signupStyles.buttonText}>Cancel</Text>
        </TouchableOpacity>
      </View>
    </LinearGradient>
  );
}

const signupStyles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    paddingHorizontal: 20,
    backgroundColor: "transparent",
  },
  logoContainer: {
    alignItems: "center",
    marginBottom: 50,
  },

  input: {
    height: 40,
    borderColor: "gray",
    borderWidth: 1,
    marginBottom: 15,
    borderRadius: 5,
    paddingHorizontal: 10,
  },
  genderContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 15,
  },
  genderText: {
    marginRight: 10,
  },
  radio: {
    marginLeft: 5,
    marginRight: 5,
  },

  radioOption: {
    padding: 10,
    borderColor: "gray",
    borderWidth: 1,
    borderRadius: 5,
    marginRight: 10,
    marginLeft: 5,
  },
  radioOptionSelected: {
    padding: 10,
    borderColor: "blue",
    borderWidth: 2,
    borderRadius: 5,
    backgroundColor: "#E6E6FA",
    marginRight: 10,
    marginLeft: 5,
  },
  buttonContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 15,
  },
  button: {
    padding: 10,
    backgroundColor: "#007BFF",
    borderRadius: 5,
    flex: 0.45,
    alignItems: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 16,
  },
});
