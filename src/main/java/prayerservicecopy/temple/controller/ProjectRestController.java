package prayerservicecopy.temple.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;


import prayerservicecopy.temple.model.Believers;
import prayerservicecopy.temple.model.Payment;
import prayerservicecopy.temple.model.Schedule;
import prayerservicecopy.temple.repository.BelieverRepository;
import prayerservicecopy.temple.repository.PaymentRepository;
import prayerservicecopy.temple.repository.ScheduleInterface;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
@Service
@Transactional
public class ProjectRestController {

	@Autowired
	private BelieverRepository believe_repo;
	
	@Autowired
	private PaymentRepository payment_repo;
	
	@Autowired
	private ScheduleInterface schedule_repo;
	
	private RazorpayClient client;
	private static final String SECRET_ID = "rzp_test_RMPvGIU1Rz6Hho";
	private static final String SECRET_KEY = "pgo8SRrOkKpzs0NQyidjMdBp";
	
	
	@PostMapping(value="/schedule")
	public Schedule createSchedule(@RequestBody Schedule schedule)
	{
		return schedule_repo.save(schedule);
		
	}
	
	@GetMapping(value = "/getScheduleDetails")
	public List<Schedule> getScheduleDetails()
	{
		return schedule_repo.findAll();
	}
	
	@GetMapping(value = "/getScheduleByDay/{dayName}")
	public Schedule getScheduleDetailsByDay(@PathVariable String dayName)
	{
		return schedule_repo.findBydayName(dayName);
		
	}
	
	@PutMapping(value="/updateScheduleByDayName/{dayName}")
	@ResponseBody
	public Schedule updateScheduleByDayName(@PathVariable String dayName, @RequestBody Schedule sched)
	{
		Schedule schedule =  schedule_repo.findBydayName(dayName);
	
		schedule.setScheduleDetails(sched.getScheduleDetails());
		schedule.setDate(sched.getDate());
		
		Schedule updatedSchedule = schedule_repo.save(schedule);
		
		return updatedSchedule;
	}
	
	@PostMapping(value = "/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Payment data) throws RazorpayException
	{
		
		System.out.println("Entered into this condition");
		
	//	System.out.println(data);
		
		int amt = Integer.parseInt(data.getAmount());
		
		int amt1 = Integer.parseInt(data.getAmount())/100;
		
		String personName = data.getPersonName();
		
		System.out.println("amount : " + amt1);
		
		System.out.println("personName : " + personName);
		
		RazorpayClient client = new RazorpayClient(SECRET_ID, SECRET_KEY); //added Object <=> var
		
			 JSONObject object = new JSONObject();
			 object.put("amount", amt);
			 object.put("currency", "INR");
			 object.put("receipt", "txn" + amt * 100);
			 
			 // now create new order
			 
			 Order order = client.orders.create(object);
 
			 
			 String orderId = order.get("id").toString();
			 
			 data.setOrderId(orderId);
			 data.setAmount(Integer.toString(amt1));
			 
			 payment_repo.save(data);
			 
			 /*
			  * 1.Order_id
			  * 2.personname
			  * 3.Amount
			  * 4.Payment_id
			  * 5.Payment_signature_Id
			  * 6.status
			  * 
			  */

		     return order.toString();
	}
	
	@GetMapping("/getOrders/{orderId}")
	@CrossOrigin(value = "*")
	public Payment getOrders(@PathVariable String orderId)
	{
		Payment payment = payment_repo.findByorderId(orderId);
		return payment;
	}
	
	@GetMapping("/getDonars/{paymentstatus}")
	public List<Payment> getDonars(@PathVariable String paymentstatus)
	{
		List<Payment> payment = payment_repo.findBypaymentStatus(paymentstatus);
		
		return payment;
		
	}
	
	@GetMapping("/getProfile/{profileNumber}")
	public Believers getProfile(@PathVariable char[] profileNumber)
	{
		Believers profile = believe_repo.findByProfileNumber(profileNumber);
		
		return profile;
	}
	
	@PutMapping("/updateProfile/{profileNumber}")
	@ResponseBody
	public Believers updateProfile(@PathVariable char[] profileNumber, @RequestBody Believers believ)
	{
		Believers believer = believe_repo.findByProfileNumber(profileNumber);
		
		believer.setBelieverName(believ.getBelieverName());
		believer.setBelieverPhone(believ.getBelieverPhone());
		believer.setBelieverLocation(believ.getBelieverLocation());
		
		Believers updateProfile = believe_repo.save(believer);
		
		return updateProfile;
		
	}
	
	@DeleteMapping("/delete-profile/{profileNumber}")
	public HashMap<String,Boolean> deleteProfile(@PathVariable char[] profileNumber)
	{
		Believers believers = believe_repo.findByProfileNumber(profileNumber);
		
		believe_repo.delete(believers);
		
		HashMap<String,Boolean> map = new HashMap();
		map.put("Is deleted",true);
		return map;
		
	}
	
	@DeleteMapping(value = "/deleteSchedule/{dayName}")
	public HashMap<String,Boolean> deleteSchedule(@PathVariable String dayName)
	{
		Schedule schedule = schedule_repo.findBydayName(dayName);
		
		schedule_repo.delete(schedule);
		
		HashMap<String,Boolean> map = new HashMap<String,Boolean>();
		
		map.put("Is Delete", true);
		
		return map;
		
	}
	
	@PutMapping("/updatePayment/{orderId}")
	public Payment updatePayment(@PathVariable String orderId, @RequestBody Payment pay)
	{
		System.out.println("passed upto above select query");
		Payment payment = payment_repo.findByorderId(orderId);
		System.out.println("passed upto below select query");
		payment.setPaymentId(pay.getPaymentId());
		System.out.println("from body passed value of Paymentid : " + pay.getPaymentId());
		payment.setPaymentSignature(pay.getPaymentSignature());
		System.out.println("from body passed value of Paymentid : " + pay.getPaymentSignature());
		payment.setPaymentStatus(pay.getPaymentStatus());
		System.out.println("from body passed value of status : " + pay.getPaymentStatus());
		
		Payment updatedPaymentDetails  = payment_repo.save(payment);
		
		return updatedPaymentDetails;
		
	}
	
	@GetMapping("/believers")
	public List<Believers> getPersons()
	{
		return believe_repo.findAll();
	}
	
	
	@PostMapping("/believers")
	@ResponseBody
	public char[] createMember(@RequestBody Believers believer)
	{
		String numbers = "1234567890";
		Random random = new Random();
		int length = 9;
		
		char[] otp = new char[length];
		
		for(int i = 0;i<length;i++)
		{
			otp[i] = numbers.charAt(random.nextInt(numbers.length()));
		}
		
		believer.setProfileNumber(otp);
		
		believe_repo.save(believer);
	
		return believer.getProfileNumber();
	}
	
	@PostMapping(value="/imageUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String imgUpload(@RequestParam MultipartFile file) throws InvalidKeyException, URISyntaxException, StorageException, IOException
	{
        String AzureCred = "DefaultEndpointsProtocol=https;AccountName=jerusalemstorageaccount;AccountKey=vyJ890j1SMlOeHFjYHzRXc3aHlqEy3+LT8b9cu6QVxNHJ7Y3tgo1XWMu8JpxqyjMjn0tUHBVGg5U+AStJLVaSQ==;EndpointSuffix=core.windows.net";
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(AzureCred);
		
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		System.out.println("CloudBlobClient got created");
		CloudBlobContainer conatiner = blobClient.getContainerReference("images-gzl-tmple");
		
		BlobContainerPermissions conatinerPermissions = new BlobContainerPermissions();
		
		conatinerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
		
		conatiner.uploadPermissions(conatinerPermissions);
		
		CloudBlockBlob blob = conatiner.getBlockBlobReference(file.getOriginalFilename());
		
		blob.getProperties().setContentType("image/jpg,image/png,image/gif,image/jpeg");//The field file exceeds its maximum permitted size of 1048576 bytes
		
		System.out.println("File Uploading started");
		
		blob.upload(file.getInputStream(), file.getSize());
		
		System.out.println("File Upload completed successfully");
		
		return "Success";
	}
	
	@PostMapping(value = "/uploadVideo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadVideo(@RequestParam MultipartFile file) throws InvalidKeyException, URISyntaxException, StorageException, IOException
	{
		String AzureCred = "DefaultEndpointsProtocol=https;AccountName=jerusalemstorageaccount;AccountKey=vyJ890j1SMlOeHFjYHzRXc3aHlqEy3+LT8b9cu6QVxNHJ7Y3tgo1XWMu8JpxqyjMjn0tUHBVGg5U+AStJLVaSQ==;EndpointSuffix=core.windows.net";
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(AzureCred);
		
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		
        CloudBlobContainer conatiner = blobClient.getContainerReference("videos-gzl-tmple");
		
		BlobContainerPermissions conatinerPermissions = new BlobContainerPermissions();
		
		conatinerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
		
		conatiner.uploadPermissions(conatinerPermissions);
		
        CloudBlockBlob blob = conatiner.getBlockBlobReference(file.getOriginalFilename());
		
		blob.getProperties().setContentType("audio/mp4");//The field file exceeds its maximum permitted size of 1048576 bytes
		
		System.out.println("File Uploading started");
		
		blob.upload(file.getInputStream(), file.getSize());
		
		System.out.println("File Upload completed successfully");
		
		return "success";
	}
	
	
	
	@GetMapping("/getVideoBlobs")
	public ArrayList<String> getVideoBlobs() throws InvalidKeyException, URISyntaxException, StorageException
	{
		
		ArrayList<String> blobs = new ArrayList<String>();
       
		String AzureCred = "DefaultEndpointsProtocol=https;AccountName=jerusalemstorageaccount;AccountKey=vyJ890j1SMlOeHFjYHzRXc3aHlqEy3+LT8b9cu6QVxNHJ7Y3tgo1XWMu8JpxqyjMjn0tUHBVGg5U+AStJLVaSQ==;EndpointSuffix=core.windows.net";
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(AzureCred);
		
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		
        CloudBlobContainer conatiner = blobClient.getContainerReference("videos-gzl-tmple");
        
        for(ListBlobItem blobItem : conatiner.listBlobs())
        {
            blobs.add(blobItem.getUri().toString());
        	System.out.println(blobItem.getUri());
        
        }
        
        return blobs;
	}
	
	@GetMapping("/getImageBlobs")
	@CrossOrigin("*")
	public ArrayList<String> getImgaeBlobs() throws InvalidKeyException, URISyntaxException, StorageException
	{
		ArrayList<String> blobs = new ArrayList<String>();
	       
		String AzureCred = "DefaultEndpointsProtocol=https;AccountName=jerusalemstorageaccount;AccountKey=vyJ890j1SMlOeHFjYHzRXc3aHlqEy3+LT8b9cu6QVxNHJ7Y3tgo1XWMu8JpxqyjMjn0tUHBVGg5U+AStJLVaSQ==;EndpointSuffix=core.windows.net";
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(AzureCred);
		
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		
        CloudBlobContainer container = blobClient.getContainerReference("images-gzl-tmple");
        
        for(ListBlobItem blobItem : container.listBlobs())
        {
        	blobs.add(blobItem.getUri().toString());
        	
        	System.out.println(blobItem.getUri());
        }
        
        return blobs;
	}
	
}

