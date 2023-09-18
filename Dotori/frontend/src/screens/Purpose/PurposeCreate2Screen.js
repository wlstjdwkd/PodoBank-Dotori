import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Image,
  TouchableWithoutFeedback,
} from "react-native";
import { AntDesign } from "@expo/vector-icons";
// import { Calendar } from "react-native-calendars";
import HeaderComponent from "../Components/HeaderScreen";

export default function PurposeCreate2Screen({ navigation }) {
  const [purposeInfo, setPurposeInfo] = useState({
    purposeName: "",
  });
  const [isValid, setIsValid] = useState(false);
  const handleNameChange = (text) => {
    setPurposeInfo((prev) => ({ ...prev, purposeName: text }));
  };
  return (
    <View style={styles.container}>
      <HeaderComponent
        title="목표생성(2/3)"
        cancelNavi="PurposeScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.header}>
        <Text style={styles.title}>목표 기간 설정하기</Text>
        <Text style={styles.subtitle}>
          목표 시작 날짜와 종료 날짜를 선택해주세요.
        </Text>

        <Text style={styles.dateText}>시작 날짜</Text>
        <TextInput
          style={styles.input}
          onChangeText={handleNameChange}
          multiline={true}
        />

        <Text style={styles.dateText}>종료 날짜</Text>
        <TextInput
          style={styles.input}
          onChangeText={handleNameChange}
          multiline={true}
        />
      </View>

      <TouchableOpacity
        style={styles.button}
        onPress={() =>
          navigation.navigate("PurposeCreate2Screen", {
            purposeInfo: purposeInfo,
          })
        }
        //TODO: 풀기
        // disabled={!isValid}
      >
        <Text style={styles.buttonText}>다음</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "space-between",
    padding: 20,
    backgroundColor: "white",
  },
  header: {
    flex: 1,
    // justifyContent: "center",
    // alignItems: "center",
    marginTop: 90,
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
  },
  subtitle: {
    color: "#7B7B7B",
    marginBottom: 30,
  },
  input: {
    width: "100%",
    height: 50,
    backgroundColor: "#D9D9D920",
    borderWidth: 1,
    borderColor: "#BAC0CA",
    borderRadius: 10,
    padding: 10,
    marginTop: 10,
    marginBottom: 40,
    // textAlign: "center",
  },
  button: {
    height: 50,
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
  dateText: {
    fontSize: 18,
  },
});
