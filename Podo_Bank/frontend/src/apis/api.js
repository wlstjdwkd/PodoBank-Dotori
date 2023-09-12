// src/api/api.js
import axios from 'axios';

export const dataExam1 = async () => {
  try {
    console.log('되나')
    const response = await axios.get('https://codingapple1.github.io/shop/data2.json');
    console.log('굳')
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('API 호출 실패:', error);
    throw error;
  }
};


// //예시) 사용시 사용 부분에서 아래처럼 사용
// import { dataExam1 } from '../api/api'

// // api를 위해서 작성했음.
// const [isLoading, setIsLoading] = useState(false);
// const [apiData, setApiData] = useState(null);
// const handleDataExam1 = async () => {
//   setIsLoading(true);
//   try {
//     const data = await dataExam1();
//     console.log(data)
//     setApiData(data);
//   } catch (error) {
//     // 에러 처리
//   } finally {
//     setIsLoading(false);
//   }
// };

// // UI 표기 {/* API 데이터 표시 */}
// {isLoading && <Text>로딩 중...</Text>}
// {apiData && (
//   <View>
//     <Text>API 데이터:</Text>
//     <Text>{JSON.stringify(apiData, null, 2)}</Text>
//   </View>
// )}