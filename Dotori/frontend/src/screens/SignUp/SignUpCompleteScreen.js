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

export default function SignUpCompleteScreen({ navigation, route }) {
  const name = route.params.name;

  return (
    <View style={styles.container}>
      <View style={styles.centerContainer}>
        <Image
          style={styles.centerImage}
          source={require("../../assets/images/Hamster/SignUpHamster.png")}
        />
        <Text style={styles.boldText}>{name}님</Text>
        <Text style={styles.regularText}>회원가입을 축하합니다!</Text>
      </View>

      <TouchableOpacity
        style={styles.button}
        onPress={() => navigation.navigate("MainPageScreen")}
      >
        <Text style={styles.buttonText}>도토리 시작하기</Text>
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
  centerContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  centerImage: {
    width: 200, // 이미지의 너비
    height: 200, // 이미지의 높이
    marginBottom: 40,
  },
  boldText: {
    fontWeight: "bold",
    fontSize: 30,
    marginBottom: 10,
  },
  regularText: {
    fontSize: 20,
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
});
