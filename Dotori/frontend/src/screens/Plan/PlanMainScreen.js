import React from "react";
import { View, Text, Image, StyleSheet, TouchableOpacity } from "react-native";
import FooterScreen from "../Components/FooterScreen";

export default function PlanMainScreen({ navigation }) {
  // TODO: 서버에서 데이터를 가져와 아래 변수들을 설정하세요
  const accountName = "월급 통장";
  const accountMoney = 0;
  const categoryList = []; // 예시: categoryList가 없는 경우는 빈 배열
  const formatNumber = (num) => {
    return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
  };
  return (
    <View style={styles.container}>
      <Text style={styles.accountName}>{accountName}</Text>
      <Text style={styles.accountMoney}>{formatNumber(accountMoney)}원</Text>

      <View style={styles.divider}></View>
      {/* <View style={styles.innerContainer}> */}
      {categoryList.length === 0 && (
        <>
          <Text style={styles.boldMessage}>새로운 계획을 생성하세요.</Text>
          <Text style={styles.message}>당신의 새로운 계획은 무엇인가요?</Text>
          {/* 이미지를 가져올 경로를 적용하세요 */}
          <Image
            style={styles.image}
            source={require("../../assets/images/Hamster/PlanMainHamster.png")}
          />
          <TouchableOpacity
            style={styles.button}
            onPress={() => {
              navigation.navigate("PlanCreate1Screen");
            }}
          >
            <Text style={styles.buttonText}>계획 생성하기</Text>
          </TouchableOpacity>
        </>
      )}
      {/* </View> */}

      <View style={styles.footer}>
        <FooterScreen navigation={navigation} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "white",
    padding: 16,
  },
  //   innerContainer: {
  //     flex: 1,
  //     alignItems: "center",
  //     justifyContent: "center",
  //     backgroundColor: "white",
  //     padding: 16,
  //     width: "100%"
  //   },
  divider: {
    width: "100%", // 원하는 너비로 조정하세요
    height: 1, // 원하는 높이로 조정하세요
    backgroundColor: "#d1d1d1", // 원하는 색상으로 변경하세요
    borderBottomLeftRadius: 25,
    borderBottomRightRadius: 25,
    marginBottom: 50, // 원하는 여백으로 조정하세요
    marginTop: -30,
  },
  accountName: {
    fontSize: 18,
    // fontWeight: "bold",
    marginBottom: 8,
    marginTop: 100,
  },
  accountMoney: {
    fontSize: 30,
    fontWeight: "bold",
    marginBottom: 100,
  },
  boldMessage: {
    fontSize: 22,
    fontWeight: "bold",

    textAlign: "center",
    marginBottom: 8,
  },
  message: {
    fontSize: 15,
    textAlign: "center",
    marginBottom: 20,
  },
  image: {
    width: 180, // 이미지 크기는 조정하세요
    height: 200, // 이미지 크기는 조정하세요
    marginBottom: 40,
    marginTop: 40,
  },
  button: {
    backgroundColor: "#FF965C",
    borderRadius: 8,
    padding: 16,
    marginBottom: 16,
    width: "100%",
  },
  buttonText: {
    fontSize: 18,
    color: "white",
    textAlign: "center",
  },
  footer: {
    flex: 1,
    justifyContent: "flex-start",
    alignItems: "center",
    marginBottom: -20,
  },
});
