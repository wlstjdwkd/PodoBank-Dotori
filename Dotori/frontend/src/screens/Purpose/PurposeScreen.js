import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  Image,
  StyleSheet,
  FlatList,
  TouchableOpacity,
} from "react-native";
import FooterScreen from "../Components/FooterScreen";
import { useDispatch, useSelector } from "react-redux";
import { purposeGetList } from "../../apis/purposeapi";
import { useIsFocused } from "@react-navigation/native";

export default function PurposeScreen({ navigation }) {
  // 토큰
  const grantType = useSelector((state) => state.user.grantType);
  const accessToken = useSelector((state) => state.user.accessToken);
  const refreshToken = useSelector((state) => state.user.refreshToken);
  const dispatch = useDispatch();
  // 그 외
  const isFocused = useIsFocused();

  const [currentTotalSavings, setcurrentTotalSavings] = useState(0);
  const [purposeList, setPurposeList] = useState([]);

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

  const getRandomBorderColor = () => {
    const colors = ["#1DA9F8", "#30D71F", "#FF77E1"];
    const randomIndex = Math.floor(Math.random() * colors.length);
    return colors[randomIndex];
  };

  const doPurposeGetList = async () => {
    try {
      const response = await purposeGetList(accessToken, grantType);
      if (response.status === 200) {
        console.log("목표 리스트 조회 성공");
        console.log(response.data);
        setPurposeList(response.data.purposeList);
        setcurrentTotalSavings(response.data.currentTotalSavings);
      } else {
        console.log("목표 리스트 조회 실패", response.status);
      }
    } catch (error) {
      console.log("오류 발생: 목표 리스트 조회 실패", error);
    }
  };

  useEffect(() => {
    if (isFocused) {
      doPurposeGetList();
    }
  }, [isFocused]);

  return (
    <View style={styles.container}>
      {/* 상단 이미지 */}
      <Image
        style={styles.topImage}
        source={require("../../assets/images/dotori_logo.png")}
      />

      {/* 최신순 & 저축액 */}
      <View style={styles.dateAmountContainer}>
        <TouchableOpacity>
          <Text style={styles.latest}>최신순</Text>
        </TouchableOpacity>
        <View style={styles.colContainer}>
          <Text style={styles.amount}>{currentTotalSavings}원</Text>
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
        style={{ marginBottom: 80 }}
        data={purposeList}
        renderItem={({ item }) => {
          const borderColor = getRandomBorderColor();
          return (
            <TouchableOpacity
              style={[styles.targetContainer, { borderColor }]}
              onPress={() => {
                if (item.terminatedAt !== null) {
                  navigation.navigate("PurposeEnd1Screen", {
                    purposeData: item,
                  });
                } else {
                  navigation.navigate("PurposeDetailScreen", {
                    purposeSeq: item.purposeSeq,
                  });
                }
              }}
            >
              <Text style={styles.targetName}>{item.title}</Text>
              <View style={styles.rightAlignContainer}>
                <Text style={styles.currentAmount}>
                  {item.currentBalance.toLocaleString()}원
                </Text>
              </View>
              <View style={styles.targetAmounts}>
                <Text style={styles.targetAmountText}>목표 금액</Text>
                <View style={styles.rightAlignContainer}>
                  <Text style={styles.targetAmount}>
                    {item.goalAmount.toLocaleString()}원
                  </Text>
                </View>
              </View>
            </TouchableOpacity>
          );
        }}
        keyExtractor={(item) => item.purposeSeq}
        ListFooterComponent={
          <TouchableOpacity
            style={styles.addButton}
            onPress={() => navigation.navigate("PurposeCreate1Screen")}
          >
            <Text style={styles.addText}>+</Text>
          </TouchableOpacity>
        }
      />

      {/* 목표 리스트 */}
      {/* <FlatList
        data={data}
        renderItem={({ item }) => {
          const borderColor = getRandomBorderColor();
          return (
            <TouchableOpacity style={[styles.targetContainer, { borderColor }]}
              onPress={()=>{
                navigation.navigate("PurposeDetailScreen", {itemId:item.id})
              }}
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
          );
        }}
        keyExtractor={(item) => item.id}
        ListFooterComponent={
          <TouchableOpacity
            style={styles.addButton}
            onPress={() => navigation.navigate("PurposeCreate1Screen")}
          >
            <Text style={styles.addText}>+</Text>
          </TouchableOpacity>
        }
      /> */}
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
    textAlign: "right",
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
    marginHorizontal: 15,
    marginTop: 20,
    flexDirection: "column",

    elevation: 5,
    backgroundColor: "white",
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
    marginHorizontal: 15,
    height: 70,
    marginTop: 20,
    borderStyle: "dashed", // 점선 테두리 추가
  },
  addText: {
    color: "#A6A6A6",
    fontSize: 24,
  },
});
