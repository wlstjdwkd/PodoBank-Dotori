import React from "react";
import {
  View,
  Text,
  StyleSheet,
  Button,
  TouchableOpacity,
  FlatList,
  Image,
} from "react-native";
import { Ionicons, EvilIcons } from "@expo/vector-icons";
import { LinearGradient } from "expo-linear-gradient";

import HeaderScreen from "../Header/HeaderScreen";
import BottomBarScreen from "../Header/BottomBarScreen";

// 더미 데이터 (실제 데이터와 연동 필요)
const accountDetail = {
  nickname: "나의 첫번째 계좌",
  number: "1234-5678-9101",
  balance: 10000,
  transactions: [
    {
      id: "1",
      date: "2023.05.30 15:30:00",
      place: "카페",
      type: "출금",
      amount: 500,
      afterBalance: 9500,
    },
    // ... 필요하면 더 추가
  ],
};

export default function AccountDetail({ navigation }) {
  // 현재 날짜를 가져오기
  const currentDate = new Date();

  // 3개월 전 날짜를 계산
  let threeMonthsAgo = new Date(currentDate);
  threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);

  // 날짜를 "YYYY.MM.DD" 형식으로 변환하는 함수
  const formatDate = (date) => {
    return `${date.getFullYear()}.${date.getMonth() + 1}.${date.getDate()}`;
  };

  return (
    <View style={styles.container}>
      {/* <HeaderScreen navigation={navigation} title="계좌 상세 조회" /> */}
      <View style={styles.headerContainer}>
        <Image
          source={require("../../assets/images/normal_podo.png")}
          style={styles.podoImage}
        />
        <Text style={styles.bankText}>포도은행</Text>
      </View>
      <View style={styles.nicknameContainer}>
        <Text style={styles.nickname}>{accountDetail.nickname}</Text>
        <TouchableOpacity>
          <EvilIcons
            name="pencil"
            size={20}
            color="black"
            style={{ marginBottom: -15 }}
          />
        </TouchableOpacity>
      </View>

      <Text style={styles.accountNumber}>{accountDetail.number}</Text>
      <Text style={styles.balance}>
        {accountDetail.balance.toLocaleString()}원
      </Text>

      {/* <View style={styles.transferButtonContainer}>
        <Button
          title="이체하기"
          onPress={() => {
            navigation.navigate("TransferScreen");
          }}
        />
      </View> */}
      <View style={styles.horizontalSeparator} />
      {/* 계좌 내역 조회 방식 선택 */}
      <View style={styles.queryModeContainer}>
        <Text>3개월 · </Text>
        <Text>전체 · </Text>
        <Text>최신순</Text>
        <Ionicons name="chevron-forward" size={24} color="black" />
      </View>
      <View style={styles.horizontalSeparator} />

      {/* 여기부터는 계좌 내역 관련 UI */}
      <View style={styles.dateContainer}>
        <Text style={styles.dateText}>
          {formatDate(threeMonthsAgo)} ~ {formatDate(currentDate)}
        </Text>
      </View>
      <View style={styles.horizontalSeparator} />
      <FlatList
        data={accountDetail.transactions}
        renderItem={({ item }) => (
          <View style={styles.transactionContainer}>
            <Text style={styles.transactionDate}>{item.date}</Text>
            <Text style={styles.place}>{item.place}</Text>
            <View style={styles.transactionAmountContainer}>
              <Text style={styles.transactionType}>{item.type} </Text>
              <Text style={styles.transactionAmount}>
                {item.amount.toLocaleString()}
              </Text>
              <Text>원</Text>
            </View>
            <Text style={styles.afterBalance}>
              잔액: {item.afterBalance.toLocaleString()}원
            </Text>
            <View style={styles.separator} />
          </View>
        )}
        keyExtractor={(item) => item.id}
      />
    </View>
  );
}

// 스타일 정의
const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: "white",
  },
  headerContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "flex-start",
    paddingTop: 30,
  },
  podoImage: {
    width: 25,
    height: 25,
    resizeMode: "contain",
    marginLeft: 20,
    marginRight: 15,
  },
  bankText: {
    fontSize: 15,
    marginRight: 15,
  },

  nicknameContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginTop: 20,
    marginLeft: 20,
    marginBottom: 5,
  },

  nickname: {
    fontSize: 16,
    // fontWeight: "bold",
    textAlign: "left",
    marginTop: 20,
  },
  accountNumber: {
    fontSize: 20,
    textAlign: "left",
    fontWeight: "bold",
    marginBottom: 40,
    marginLeft: 20,
  },
  balance: {
    fontSize: 24,
    fontWeight: "bold",
    textAlign: "right",
    marginRight: 20,
    marginBottom: 20,
  },
  transferButtonContainer: {
    justifyContent: "center",
    alignItems: "center",
    marginVertical: 20,
  },
  transactionDate: {
    fontSize: 16,
    marginBottom: 5,
  },
  place: {
    fontSize: 16,
    marginBottom: 5,
  },
  afterBalance: {
    textAlign: "right",
    fontSize: 16,
    marginBottom: 20,
  },
  separator: {
    height: 1,
    backgroundColor: "gray",
    marginVertical: 10,
  },
  transactionContainer: {
    flexDirection: "column",
  },
  transactionType: {
    fontSize: 16,
    textAlign: "right",
    flex: 1,
  },
  transactionAmount: {
    color: "red",
  },
  transactionAmountContainer: {
    flexDirection: "row",
    textAlign: "right",
    justifyContent: "flex-end",
    alignItems: "center",
  },
  dateContainer: {
    marginTop: 20,
    marginBottom: 10,
    alignItems: "center",
  },
  dateText: {
    fontSize: 16,
    marginBottom: 10,
    // justifyContent: "flex-start",
  },
  horizontalSeparator: {
    height: 1,
    backgroundColor: "grey",
    marginVertical: 10,
  },
  queryModeContainer: {
    flexDirection: "row",
    justifyContent: "flex-start",
    alignItems: "center",
  },
});
