import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Image,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";

export default function PurposeCreate3Screen({ navigation, route }) {
  const [purposeInfo, setPurposeInfo] = useState(route.params.purposeInfo);
  const [isValid, setIsValid] = useState(false);
  const handleMoneyChange = (text) => {
    setPurposeInfo((prev) => ({ ...prev, purposeMoney: text }));
    if (text.length > 0) {
      setIsValid(true);
    } else {
      setIsValid(false);
    }
  };
  return (
    <View style={styles.container}>
      <HeaderComponent
        title="목표생성(3/3)"
        cancelNavi="PurposeScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.innerContainer}>
        <View style={styles.header}>
          <Text style={styles.title}>목표 금액 설정하기</Text>
          <Text style={styles.subtitle}>목표 금액을 숫자로 적어주세요.</Text>

          <TextInput
            style={styles.input}
            onChangeText={handleMoneyChange}
            multiline={true}
            placeholder="예시) 10000"
            keyboardType="numeric"
          />
          <View style={styles.textRight}>
            <Text style={styles.instructionText}>숫자만 적어주세요.</Text>
          </View>
        </View>

        <TouchableOpacity
          style={styles.button}
          onPress={() =>
            navigation.navigate("PurposeCompleteScreen", {
              name: purposeInfo.purposeName,
            })
          }
          //TODO: 풀기
          // disabled={!isValid}
        >
          <Text style={styles.buttonText}>목표 생성하기</Text>
        </TouchableOpacity>
      </View>
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
  innerContainer: {
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
    marginBottom: 10,
    marginTop: 20,
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
  imageView: {
    justifyContent: "center",
    alignItems: "center",
    marginTop: 30,
    marginBottom: 40,
  },
  middleImage: {
    // justifyContent: "center",
    // alignItems: "center",
    width: 150,
    height: 150,
  },
  textRight: {
    alignItems: "flex-end",
  },
  instructionText: {
    color: "#A9A9A9",
  },
});
