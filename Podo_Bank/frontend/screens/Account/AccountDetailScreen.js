import React from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  FlatList,
} from "react-native";
import { Ionicons, EvilIcons } from "@expo/vector-icons";

import HeaderScreen from "../Header/HeaderScreen";

// 더미 데이터 (실제 데이터와 연동 필요)
const accountDetail = {
  nickname: "나의 첫번째 계좌",
  number: "1234-5678-9101",
  balance: 10000,
  transactions: [
    {
      id: "1",
      date: "2023.05.30 15:30:00",
      place: "고용노동부구미시청",
      type: "출금",
      amount: 500,
      afterBalance: 9500,
    },
    {
      id: "2",
      date: "2023.05.30 15:30:00",
      place: "용돈",
      type: "입금",
      amount: 500,
      afterBalance: 9500,
    },
    {
      id: "3",
      date: "2023.05.30 15:30:00",
      place: "티머니개인택시",
      type: "출금",
      amount: 20000,
      afterBalance: 11500,
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
      <HeaderScreen navigation={navigation} title="거래 내역 조회" />
      <View style={styles.nicknameContainer}>
        <View style={styles.row}>
          <Text style={styles.nickname}>{accountDetail.nickname}</Text>
          <TouchableOpacity>
            <EvilIcons
              name="pencil"
              size={20}
              color="black"
              style={{ marginTop: 20 }}
            />
          </TouchableOpacity>
        </View>

        <TouchableOpacity
          style={styles.setting}
          onPress={() => {
            navigation.navigate("AccountManagementScreen");
          }}
        >
          <EvilIcons name="gear" size={24} color="black" />
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
        <Text style={styles.queryText}>3개월 · </Text>
        <Text style={styles.queryText}>전체 · </Text>
        <Text style={styles.queryText}>최신순</Text>
        <Ionicons name="chevron-forward" size={24} color="black" />
      </View>
      <View style={styles.horizontalSeparator} />

      {/* 여기부터는 계좌 내역 관련 UI */}
      <View style={styles.dateContainer}>
        <Text style={styles.dateText}>
          {formatDate(threeMonthsAgo)} ~ {formatDate(currentDate)}
        </Text>
      </View>
      <View style={styles.boldSeparator} />
      <FlatList
        data={accountDetail.transactions}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.cardContainer}
            onPress={() => {
              navigation.navigate("TransactionDetailScreen");
            }}
          >
            <Text style={styles.transactionDate}>{item.date}</Text>
            <Text style={styles.place}>{item.place}</Text>
            <Text style={styles.transactionInfo}>
              {item.type + " "}
              <Text
                style={
                  item.type === "출금"
                    ? styles.withdrawalAmount
                    : styles.depositAmount
                }
              >
                {item.amount.toLocaleString()}원
              </Text>
            </Text>
            <Text style={styles.afterBalance}>
              잔액: {item.afterBalance.toLocaleString()}원
            </Text>
            <View style={styles.horizontalSeparator} />
            {/* 각 아이템 밑에 수평 구분선 추가 */}
          </TouchableOpacity>
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
  row: {
    flexDirection: "row",
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
  setting: {
    alignItems: "flex-end",
  },

  nicknameContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between", // 여기를 수정했습니다.
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
    color: "gray",
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
    marginTop: 5,
    // marginBottom: 5,
    alignItems: "flex-start",
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
  boldSeparator: {
    height: 2,
    backgroundColor: "black",
    marginBottom: 20,
  },
  queryModeContainer: {
    flexDirection: "row",
    justifyContent: "flex-start",
    alignItems: "center",
  },
  queryText: {
    fontWeight: "bold",
  },
  cardContainer: {
    width: 360,
    height: 100,
    borderRadius: 23,
    marginBottom: 15, // 카드 간격
    // padding: 10, // 카드 내부 패딩
    alignSelf: "center", // 가운데 정렬
    // marginLeft: -5,
    // marginRight: -5,
  },
  withdrawalAmount: {
    color: "red",
  },
  depositAmount: {
    color: "blue",
  },
  place: {
    fontWeight: "600",
    fontSize: 14,
    lineHeight: 17,
    color: "#000000",
    marginBottom: 5,
  },
  transactionDate: {
    fontWeight: "600",
    fontSize: 12,
    lineHeight: 15,
    textAlign: "right",
    color: "#000000",
    marginBottom: 5,
    textAlign: "left", // 왼쪽 정렬
  },
  transactionInfo: {
    fontWeight: "600",
    fontSize: 14,
    lineHeight: 17,
    textAlign: "right",
    color: "#000000",
    marginBottom: 5,
  },
  afterBalance: {
    fontWeight: "600",
    fontSize: 12,
    lineHeight: 15,
    textAlign: "right",
    color: "#858585",
  },
});
