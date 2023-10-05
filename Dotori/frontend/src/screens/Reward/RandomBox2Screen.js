import React, { useState } from "react";
import {
  View,
  Text,
  Image,
  StyleSheet,
  TouchableOpacity,
  Dimensions,
} from "react-native";

export default function RandomBox1Screen({ navigation, route }) {
  const coin = route.params.coin;

  // 선택한 코인의 상태를 관리합니다. 0은 선택하지 않은 상태입니다.
  const [selectedCoin, setSelectedCoin] = useState(0);

  const handleCoinSelect = (coinValue) => {
    setSelectedCoin(coinValue);
  };

  const handleOpen = () => {
    navigation.navigate("RandomBox2Screen", {
      selectedCoinValue: selectedCoin,
    });
  };
  return (
    <View style={styles.container}>
      <Text style={styles.headerText}>꽝 없는 랜덤박스</Text>
      <Text style={styles.infoText}>
        코인 1개, 2개, 3개로 랜덤박스를 뽑을 수 있어요.
      </Text>
      <Text style={styles.infoText}>
        코인을 많이 줄수록 더 좋은 상품이 나온답니다.
      </Text>
      <View style={styles.imagesContainer}>
        <TouchableOpacity onPress={() => handleCoinSelect(1)}>
          <Image
            source={require("../../assets/images/coin1.png")}
            style={[styles.image, selectedCoin !== 1 && styles.dimImage]}
          />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => handleCoinSelect(2)}>
          <Image
            source={require("../../assets/images/coin2.png")}
            style={[styles.image, selectedCoin !== 2 && styles.dimImage]}
          />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => handleCoinSelect(3)}>
          <Image
            source={require("../../assets/images/coin3.png")}
            style={[styles.image, selectedCoin !== 3 && styles.dimImage]}
          />
        </TouchableOpacity>
      </View>
      <TouchableOpacity style={styles.button} onPress={handleOpen}>
        <Text style={styles.buttonText}>열어보기</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: 40,
    backgroundColor: "white",
  },
  headerText: {
    fontSize: 30,
    fontWeight: "bold",
    marginBottom: 20,
  },
  infoText: {
    fontSize: 12,
    marginVertical: 10,
    color: "#757575",
  },
  imagesContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    width: Dimensions.get("window").width - 40,
    marginBottom: 20,
    marginTop: 100,
    marginLeft: -20,
  },
  image: {
    width: 80, // 이미지 크기는 적절히 조정해주세요
    height: 80,
    marginHorizontal: 10,
  },
  dimImage: {
    opacity: 0.5, // 희미하게 만들기 위한 스타일
  },
  button: {
    backgroundColor: "#FF965B",
    borderRadius: 20,
    paddingHorizontal: 40,
    paddingVertical: 10,
    alignItems: "center",
    marginTop: 100,
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
});