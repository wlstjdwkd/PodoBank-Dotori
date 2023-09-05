import React from "react";
import { View, Text, Image, StyleSheet, TouchableOpacity } from "react-native";

function SignupCompleteScreen({ navigation }) {
  return (
    <View style={styles.container}>
      {/* Header */}
      <View style={styles.headerContainer}>
        <Image
          source={require("../../assets/images/normal_podo.png")}
          style={styles.podoImage}
        />
        <Text style={styles.bankText}>포도은행</Text>
      </View>

      {/* 회원가입 완료 텍스트 */}
      <Text style={styles.completedText}>~~~님, 회원가입을 축하합니다!</Text>

      {/* 홈 화면 가기 버튼 */}
      <TouchableOpacity
        style={styles.customButton}
        onPress={() => navigation.navigate("HomeScreen")}
      >
        <Text style={styles.linkText}>시작하기</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "white",
  },
  headerContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "flex-start",
    paddingTop: 30,
    position: "absolute",
    marginTop: 20,
    top: 0,
    left: 30,
  },
  podoImage: {
    width: 25,
    height: 25,
    resizeMode: "contain",
    // marginLeft: ,
    marginRight: 15,
  },
  bankText: {
    fontSize: 15,
    marginRight: 15,
  },
  completedText: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 180,
  },
  customButton: {
    backgroundColor: "#A175FD",
    padding: 10,
    borderRadius: 5,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 10,
    marginRight: 30,
    marginLeft: 30,
    borderRadius: 5,
    width: 300,
  },
});

export default SignupCompleteScreen;
