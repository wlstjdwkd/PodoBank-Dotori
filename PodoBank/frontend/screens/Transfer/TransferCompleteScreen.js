import React from "react";
import { View, Text, Image, TouchableOpacity, StyleSheet } from "react-native";
import HeaderComponent from "../Header/HeaderScreen/"; // HeaderComponent의 경로에 맞게 수정하세요.

export default function TransferCompleteScreen({ navigation, route }) {
  const { receiverName, transferAmount } = route.params; // 넘겨받은 파라미터를 추출합니다.

  return (
    <View style={styles.container}>
      <HeaderComponent navigation={navigation} title="이체 완료" />

      {/* <Image 
        source={{ uri: 'https://example.com/your-image.jpg' }} // 원하시는 이미지 URL로 교체하세요.
        style={styles.image}
      /> */}

      <Text style={styles.largeText}>{receiverName}님께</Text>
      <Text style={styles.largeText}>{transferAmount}원</Text>
      <Text style={styles.normalText}>이체가 완료되었습니다.</Text>

      <TouchableOpacity
        style={styles.button}
        onPress={() => navigation.navigate("HomeScreen")}
      >
        <Text style={styles.buttonText}>확인</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    padding: 20,
    backgroundColor: "white",
  },
  image: {
    width: 200,
    height: 200,
    marginBottom: 30,
  },
  largeText: {
    fontSize: 28,
    fontWeight: "bold",
    marginBottom: 10,
  },
  normalText: {
    fontSize: 16,
    marginBottom: 20,
  },
  button: {
    backgroundColor: "#6BB5FF",
    paddingVertical: 10,
    paddingHorizontal: 30,
    borderRadius: 5,
  },
  buttonText: {
    color: "white",
    fontSize: 16,
    fontWeight: "bold",
  },
});
