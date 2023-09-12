import React from "react";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";

export default function SignupCompleteScreen({ navigation, route }) {
  const userName = route.params.name;
  return (
    <View style={styles.container}>
      

      {/* 회원가입 완료 텍스트 */}
      <Text style={styles.completedText}>
        {userName}님, 회원가입을 축하합니다!
      </Text>

      {/* 홈 화면 가기 버튼 */}
      <TouchableOpacity
        style={styles.customButton}
        onPress={() => navigation.navigate("LoginScreen")}
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
