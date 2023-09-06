import React from "react";
import { View, Text, StyleSheet, Image, TouchableOpacity } from "react-native";
import HeaderComponent from "../Header/HeaderScreen";

export default function OpenAccountCompleteScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <HeaderComponent navigation={navigation} title="계좌개설 완료" />
      <View style={styles.contentContainer}>
        <Image
          source={require("../../assets/images/happy_podo.png")}
          style={styles.image}
        />

        <Text style={styles.boldText}>포도은행 계좌가 신설되었습니다.</Text>

        <Text style={styles.accountNumber}>1538-1525-3123</Text>

        <TouchableOpacity
          style={styles.homeButton}
          onPress={() => navigation.navigate("HomeScreen")}
        >
          <Text style={styles.homeButtonText}>홈으로가기</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    justifyContent: "flex-start",
  },
  contentContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    width: "100%",
  },
  image: {
    width: 200,
    height: 200,
    marginBottom: 30,
  },
  boldText: {
    fontWeight: "bold",
    fontSize: 18,
    textAlign: "center",
    marginVertical: 20, // 위 아래로 간격 추가
  },
  accountNumber: {
    fontSize: 16,
    textAlign: "center",
    marginBottom: 30,
  },
  homeButton: {
    backgroundColor: "purple",
    padding: 15,
    borderRadius: 8,
    alignItems: "center",
    width: "80%", // 버튼의 넓이를 조절
  },
  homeButtonText: {
    color: "#fff",
    fontWeight: "bold",
  },
});
