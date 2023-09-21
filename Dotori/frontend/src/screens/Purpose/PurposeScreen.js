import React, { useState } from "react";
import {
  View,
  Text,
  Image,
  StyleSheet,
  FlatList,
  TouchableOpacity,
} from "react-native";
import FooterScreen from "../Components/FooterScreen";
export default function PurposeScreen({ navigation }) {
  const [nowPurposeAmount, setNowPurposeAmount] = useState(3000000);
  // 여기서 목표 데이터를 추가하세요
  const data = [
    {
      id: "1",
      name: "목표1",
      currentAmount: 2500,
      targetAmount: 10000,
    },
    {
      id: "2",
      name: "목표2",
      currentAmount: 2500,
      targetAmount: 10000,
    },
    // 다른 목표들...
  ];

  const formatNumber = (num) => {
    return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
  };

  return (
    <View style={styles.container}>
      {/* 상단 이미지 */}
      <Image
        style={styles.topImage}
        source={require("../../assets/images/dotori_logo.png")}
      />

      {/* 최신순 & 저축액 */}
      <View style={styles.dateAmountContainer}>
        <Text style={styles.latest}>최신순</Text>
        <View style={styles.colContainer}>
          <Text style={styles.amount}>{formatNumber(nowPurposeAmount)}원</Text>
          <Text style={styles.currentSavings}>현재 저축액</Text>
        </View>
      </View>

      {/* 중간 이미지 */}
      <View style={styles.middleImageContainer}>
        <Image
          style={styles.middleImage}
          source={require("../../assets/images/Hamster/PurposeHamster.png")}
        />
      </View>

      <View style={styles.divider}></View>

      {/* 목표 리스트 */}
      <FlatList
        data={data}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.targetContainer}
            onPress={() => navigation.navigate("PurposeDetailScreen", { itemId: item.id })}
          >
            <Text style={styles.targetName}>{item.name}</Text>
            <View style={styles.rightAlignContainer}>
              <Text style={styles.currentAmount}>
                {formatNumber(item.currentAmount)}원
              </Text>
            </View>
            <View style={styles.targetAmounts}>
              <Text style={styles.targetAmountText}>목표 금액</Text>
              <View style={styles.rightAlignContainer}>
                <Text style={styles.targetAmount}>
                  {formatNumber(item.targetAmount)}원
                </Text>
              </View>
            </View>
          </TouchableOpacity>
        )}
        keyExtractor={(item) => item.id}
        ListFooterComponent={
          <TouchableOpacity
            style={styles.addButton}
            onPress={() => navigation.navigate("PurposeCreate1Screen")}
          >
            <Text style={styles.addText}>+</Text>
          </TouchableOpacity>
        }
      />
      <View style={styles.footer}>
        <FooterScreen navigation={navigation} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#FFFFFF",
    paddingHorizontal: 20,
  },
  topImage: {
    alignSelf: "center",
    marginTop: 20,
    resizeMode: "contain",
    width: 70,
    height: 70,
  },
  dateAmountContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  latest: {
    // 여기에 텍스트 스타일 추가...
    backgroundColor: "#EFF3F6",
    borderRadius: 10,
    fontSize: 12,
    padding: 10,
  },
  amount: {
    // 여기에 텍스트 스타일 추가...
    fontSize: 20,
  },
  currentSavings: {
    color: "#9D9D9D",
    // 여기에 추가 스타일...
  },
  middleImageContainer: {
    position: "absolute", // Set position to absolute
    alignSelf: "center",
    zIndex: 1, // Make sure the image is on top of the divider
    top: "20%", // Adjust this to position the image correctly over the divider
  },
  middleImage: {
    // 여기에 이미지 스타일 추가...
    width: 100,
    height: 110,
  },
  divider: {
    marginTop: 115, // Height of the middleImage divided by 2, adjust this value as needed
    height: 10,
    backgroundColor: "#FFF2DE", // or any color you'd like
    marginBottom: 30, // space after the divider
  },
  targetContainer: {
    borderRadius: 15,
    borderColor: "#A6A6A6",
    borderWidth: 1,
    padding: 10,
    marginTop: 20,
    flexDirection: "column",
  },

  targetName: {
    marginBottom: 10,
    fontSize: 20,
    flex: 1,
  },

  rightAlignContainer: {
    alignItems: "flex-end",
    flex: 1,
  },

  currentAmount: {
    fontSize: 16,
    marginBottom: 10,
  },

  targetAmounts: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },

  targetAmountText: {
    fontSize: 12,
    flex: 1,
  },

  targetAmount: {
    fontSize: 12,
    textAlign: "right",
    flex: 1,
  },
  addButton: {
    justifyContent: "center",
    alignItems: "center",
    borderRadius: 15,
    borderColor: "#A6A6A6",
    borderWidth: 1,
    height: 70,
    marginTop: 20,
    borderStyle: "dashed", // 점선 테두리 추가
  },
  addText: {
    color: "#A6A6A6",
    fontSize: 24,
  },
});
