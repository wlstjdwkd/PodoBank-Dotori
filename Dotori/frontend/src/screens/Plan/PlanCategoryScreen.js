import React, { useState } from "react";
import {
  View,
  Text,
  Image,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
} from "react-native";
import { Feather } from "@expo/vector-icons";

export default function PlanCategoryScreen({ navigation }) {
  // TODO: 서버에서 데이터를 가져와 아래 변수들을 설정하세요
  const sampleData = {
    categoryName: "Travel",
    targetMoney: 500000,
    currentMoney: 250000,
    consumeList: [
      {
        transaction_at: "2023-09-15 10:30:00",
        transaction_details: "Flight ticket",
        amount: 150000,
      },
      {
        transaction_at: "2023-09-15 08:30:00",
        transaction_details: "Flight ticket",
        amount: 200000,
      },
      {
        transaction_at: "2023-09-16 13:45:00",
        transaction_details: "Hotel booking",
        amount: 100000,
      },
    ],
  };

  const [data, setData] = useState(sampleData);
  const formatNumber = (num) => {
    return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
  };
  const formatDate = (dateStr) => {
    const dateObj = new Date(dateStr);
    const month = dateObj.getMonth() + 1; // 월은 0부터 시작하므로 +1 해주어야 합니다.
    const date = dateObj.getDate();
    return `${month}월 ${date}일`;
  };

  const groupedConsumeList = () => {
    const sortedList = data.consumeList.sort(
      (a, b) => new Date(b.transaction_at) - new Date(a.transaction_at)
    );

    const grouped = sortedList.reduce((acc, curr) => {
      const date = curr.transaction_at.split(" ")[0];
      if (!acc[date]) acc[date] = [];
      acc[date].push(curr);
      return acc;
    }, {});

    return grouped;
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <View style={{ flex: 1 }} />
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Feather name="x" size={30} style={styles.closeIcon} />
        </TouchableOpacity>
      </View>

      <View style={styles.infoContainer}>
        <Text style={styles.categoryName}>{data?.categoryName}</Text>
        <Text style={styles.targetMoney}>
          목표 금액 {formatNumber(data?.targetMoney)}원
        </Text>
      </View>

      <View style={styles.moneyContainer}>
        <Text style={styles.moneyLabel}>사용 금액</Text>
        <Text style={styles.remainingMoney}>남은 금액</Text>
      </View>

      <View style={styles.moneyValuesContainer}>
        <Text style={styles.tempMoney}>0원</Text>
        <Text style={styles.currentMoney}>
          {formatNumber(data?.currentMoney)}원
        </Text>
      </View>

      <View style={styles.divider} />

      <ScrollView style={{ flex: 1, width: "120%" }}>
        <View style={{ alignItems: "center" }}>
          {Object.entries(groupedConsumeList()).map(([date, transactions]) => (
            <View key={date} style={styles.dateGroup}>
              <Text style={styles.dateText}>{formatDate(date)}</Text>
              {transactions.map((trans, index) => (
                <View key={index} style={styles.transBox}>
                  <Text style={styles.transDetails}>
                    {trans.transaction_details}
                  </Text>
                  <View style={styles.innerContainer}>
                    <Text style={styles.transTime}>
                      {trans.transaction_at.split(" ")[1].slice(0, 5)}
                    </Text>
                    <Text style={styles.transAmount}>
                      {formatNumber(trans.amount)}원
                    </Text>
                  </View>
                </View>
              ))}
            </View>
          ))}
        </View>
      </ScrollView>
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

  dateGroup: {
    marginBottom: 15,
    width: "80%",
    paddingLeft: 16,
    paddingRight: 16,
  },
  dateText: {
    fontSize: 18,
    marginBottom: 20,
    // marginLeft: 0,
  },
  transBox: {
    flexDirection: "column",
    alignItems: "flex-start", // 왼쪽 정렬
    justifyContent: "space-between",
    padding: 10,
    marginBottom: 5,
    borderRadius: 15,
    borderColor: "#E3E3E3",
    borderWidth: 1,
    width: "100%",
    paddingHorizontal: 20,

    elevation: 5,
    backgroundColor: "white",
  },
  innerContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    width: "100%",
    marginTop: 10,
  },
  transDetails: {
    fontSize: 16,
    alignSelf: "flex-start", // 왼쪽 정렬
  },
  transTime: {
    fontSize: 14,
    color: "#777777",
  },
  transAmount: {
    fontSize: 16,
    fontWeight: "bold",
    textAlign: "right", // 오른쪽 정렬
  },
  header: {
    flexDirection: "row",
    alignItems: "center",
    width: "100%",
    justifyContent: "flex-end",
    paddingVertical: 10,
    marginTop: 20,
    marginBottom: 20,
  },
  closeIcon: {
    fontWeight: "bold",
    color: "#000",
  },
  infoContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    width: "100%",
    paddingVertical: 10,
    paddingHorizontal: 30,
    // marginLeft: -100,
    // marginRight: -100,
    marginBottom: 20,
  },
  categoryName: {
    fontSize: 25,
    fontWeight: "bold",
  },
  targetMoney: {
    fontSize: 16,
    color: "#777777",
  },
  moneyContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    width: "100%",
    paddingVertical: 5,
    paddingHorizontal: 50,
  },
  moneyLabel: {
    fontSize: 16,
    color: "#777777",
  },
  remainingMoney: {
    fontSize: 16,
    color: "#777777",
  },
  moneyValuesContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    width: "100%",
    paddingBottom: 10,
    paddingHorizontal: 30,
    marginTop: 10,
  },
  currentMoney: {
    fontSize: 28,
    fontWeight: "bold",
    color: "#FF965C",
  },
  tempMoney: {
    fontSize: 28,
    fontWeight: "bold",
    color: "#858585",
  },
  divider: {
    width: "100%",
    height: 1,
    backgroundColor: "#BAC0CA",
    marginVertical: 40,
  },
});
